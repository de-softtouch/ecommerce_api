package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Category;
import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.reponse.CategoryResponse;
import com.api.ecommerceweb.repository.CategoryRepository;
import com.api.ecommerceweb.repository.OrderItemRepository;
import com.api.ecommerceweb.repository.ProductRepository;
import com.api.ecommerceweb.utils.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("PublicServiceHelper")
@RequiredArgsConstructor
@Slf4j
public class PublicService {

    private final ProductRepository productRepo;
    private final OrderItemRepository orderItemRepo;
    private final CategoryRepository categoryRepo;

    public ResponseEntity<?> getNewestProducts() {
        List<Product> newestProducts =
                productRepo.findAllByOrderByCreateDate();
        List<Object> rs = new ArrayList<>();

        for (Product product :
                newestProducts) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("name", product.getName());
            map.put("category", product.getCategory().getName());
            map.put("brand", product.getBrand().getName());
            if (product.getCoverImage() != null) {
                map.put("coverImage", product.getCoverImage().getName());
            }
            map.put("standardPrice", product.getStandardPrice());
            map.put("salesPrice", product.getSalesPrice());
            rs.add(map);
        }
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> getProducts(Map<String, String> params) {
        String name = null;
        if (!ValidatorUtil.isBlankOrNull(params.get("name")))
            name = params.get("name");

        String category = null;
        if (!ValidatorUtil.isBlankOrNull(params.get("category")))
            category = params.get("category");

        String brand = null;
        if (!ValidatorUtil.isBlankOrNull(params.get("brand")))
            brand = params.get("brand");

        Integer page;
        if (ValidatorUtil.isNumeric(params.get("page"))) {
            page = Integer.parseInt(params.get("page"));
        } else {
            page = 0;
        }
        Integer pageSize;
        if (ValidatorUtil.isNumeric(params.get("pageSize"))) {
            pageSize = Integer.parseInt(params.get("pageSize"));
        } else {
            pageSize = 100;
        }

        Double minPrice = null;
        if (ValidatorUtil.isNumeric(params.get("minPrice"))) {
            minPrice = Double.parseDouble(params.get("minPrice"));
        }
        Double maxPrice = null;
        if (ValidatorUtil.isNumeric(params.get("maxPrice"))) {
            maxPrice = Double.parseDouble(params.get("maxPrice"));
        }
        String sortBy;
        if (!ValidatorUtil.isBlankOrNull(params.get("sortBy"))) {
            sortBy = params.get("sortBy");
        } else {
            sortBy = "create_date";
        }

        String direction;
        if (!ValidatorUtil.isBlankOrNull(params.get("direction"))) {
            direction = params.get("direction");
        } else {
            direction = "DESC";
        }


        PageRequest pageable =
                PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(direction), sortBy));
        List<Product> products =
                productRepo.search(name, category, brand, minPrice, maxPrice, pageable);

        List<Object> rs = new ArrayList<>();
        for (Product product :
                products) {
            Object o = toProductBasicInfoResp(product);
            rs.add(o);
        }
        return ResponseEntity.ok(rs);

    }

    Object toProductBasicInfoResp(Product product) {
        Map<String, Object> rs = new HashMap<>();
        rs.put("id", product.getId());
        rs.put("name", product.getName());
        rs.put("standardPrice", product.getStandardPrice());
        rs.put("salesPrice", product.getSalesPrice());
        rs.put("coverImage", product.getCoverImage().getName());
        int solid = orderItemRepo.countOrderItemByProduct(product);
        rs.put("solid", solid);
        return rs;
    }

//    public ResponseEntity<?> getProducts(Map<String, Object> params) {
//        String name = (String) params.get("name");
////        if (name != null && name.length() > 0) {
//        if (name != null && name.length() > 0) {
//            name = String.format("%s", "%" + name + "%");
//        } else {
//            name = null;
//        }
//        log.info(name);
//
//        String category = (String) params.get("category");
//        if (category != null && category.length() > 0) {
//            category = String.format("%s", "%" + category + "%");
//        } else {
//            category = null;
//        }
//        log.info(category);
//
//        String brand = (String) params.get("brand");
//        if (brand != null && brand.length() > 0) {
//            brand = String.format("%s", "%" + brand + "%");
//        } else {
//            brand = null;
//        }
//        log.info(brand);
//
////        String limit = params.get("limit");
////        String minPrice = params.get("minPrice");
////        String maxPrice = params.get("maxPrice");
//        String sortBy = (String) params.get("sortBy");
//        String sortDirection = (String) params.get("direction");
//
//
//        Pageable pageable =
//                PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
//        List<Product> products =
//                productRepo.search(name, category, brand, 0.0, 1000.0, pageable);
//        List<Object> rs = new ArrayList<>();
//        for (Product product :
//                products) {
//            rs.add(product.getName());
//        }
//        return ResponseEntity.ok(rs);
//    }


    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepo.findAllByParentOrderByPosAscNameAsc(null);
        List<CategoryResponse> rs = new ArrayList();
        for (Category c :
                categories) {
            CategoryResponse category = new CategoryResponse();
            category.setId(c.getId());
            category.setName(c.getName());
            if (c.getImage() != null) {
                category.setImage(c.getImage().getName());
            }
            rs.add(category);
        }
        return rs;
    }
}
