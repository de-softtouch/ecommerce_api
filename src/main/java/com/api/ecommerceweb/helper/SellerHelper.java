package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.dto.BrandDTO;
import com.api.ecommerceweb.mapper.BrandMapper;
import com.api.ecommerceweb.model.*;
import com.api.ecommerceweb.repository.*;
import com.api.ecommerceweb.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("SellerHelper")
@RequiredArgsConstructor
public class SellerHelper {

    private final ProductRepository productRepo;
    private final BrandRepository brandRepo;
    private final ProductImageRepository productImageRepo;
    private final FileRepository fileRepo;
    private final CategoryRepository categoryRepo;
    private final VariationRepository variantRepo;
    private final ColorRepository colorRepo;
    private final SizeRepository sizeRepo;

    public ResponseEntity<?> getProduct(Long id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (optionalProduct.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                toProductDetailsResponse(optionalProduct.get())
        );
    }

    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productRepo.findAllByOrderByCreateDate();
        List<Object> rs = new ArrayList<>();
        for (Product product :
                products) {
            Map<String, Object> map = toProductDetailsResponse(product);
            //product shipping method
            rs.add(map);
        }
        return ResponseEntity.ok(
                rs
        );
    }

    private Map<String, Object> toProductDetailsResponse(Product product) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", product.getId());
        map.put("name", product.getName());
        map.put("standardPrice", product.getStandardPrice());
        map.put("salesPrice", product.getSalesPrice());
        //brand
        if (product.getBrand() != null) {
            Map<String, Object> brand = new HashMap<>();
            brand.put("id", product.getBrand().getId());
            brand.put("name", product.getBrand().getName());
            map.put("brand", brand);
        }
        //category
        if (product.getCategory() != null) {
            Map<String, Object> category = new HashMap<>();
            category.put("id", product.getCategory().getId());
            category.put("name", product.getCategory().getName());
            map.put("category", category);
        }
        //product images
        map.put("images", product.getImages() != null ?
                product.getImages().stream()
                        .map(productImage -> Map.of(
                                "pos", productImage.getPos(),
                                "name", productImage.getImage().getName(),
                                "id", productImage.getId(),
                                "type", productImage.getImage().getType()
                        ))
                        .collect(Collectors.toList()) : new ArrayList<>());
        //group variants
        Set<Color> colors = product.getColors();
        if (colors != null && !colors.isEmpty()) {
            map.put("colors", colors.stream().map(Color::getCode).collect(Collectors.toList()));
        }
        Set<Size> sizes = product.getSizes();
        if (sizes != null && !sizes.isEmpty()) {
            map.put("sizes", sizes.stream().map(Size::getSize).collect(Collectors.toList()));
        }
        //product variations
        List<Object> vResponses = new ArrayList<>();
        for (Variation variation :
                product.getVariations()) {
            HashMap<String, Object> v = new HashMap<>();
            v.put("id", variation.getId());
            v.put("price", variation.getPrice());
            v.put("qty", 12);
            //color
            Map<String, Object> color = new HashMap<>();
            color.put("id", variation.getColor().getId());
            color.put("code", variation.getColor().getCode());
            v.put("color", color);
            //size
            Map<String, Object> size = new HashMap<>();
            size.put("id", variation.getSize().getId());
            size.put("size", variation.getSize().getSize());
            v.put("size", size);
            vResponses.add(v);
        }
        map.put("variations", vResponses);
        return map;
    }

    public ResponseEntity<?> updateProduct(ProductRequest productRequest) {
        Product product;
        if (productRequest.getId() != null && productRepo.existsById(productRequest.getId())) {
            product = productRepo.getById(productRequest.getId());
        } else {
            product = new Product();
        }
        product.setStatus(productRequest.getStatus());
        product.setName(productRequest.getName());
        product.setStandardPrice(productRequest.getStandardPrice());
        product.setSalesPrice(productRequest.getSalesPrice());
        //save brand
        //create brand request not exist
        BrandRequest brandRequest = productRequest.getBrand();
        if (brandRequest != null) {
            if (brandRequest.getId() != null
                    && brandRepo.existsById(brandRequest.getId())) {
                product.setBrand(brandRepo.getById(brandRequest.getId()));
            } else if (brandRequest.getName() != null) {
                if (brandRepo.existsByName(brandRequest.getName())) {
                    Brand brand = brandRepo.getByName(brandRequest.getName());
                    product.setBrand(brand);
                } else {
                    Brand brand = new Brand();
                    brand.setName(brandRequest.getName());
                    brand = brandRepo.save(brand);
                    product.setBrand(brand);
                }

            }
        }

        //save product
        product = productRepo.save(product);

        //find category and save if existed
        if (productRequest.getCategoryId() != null) {
            Optional<Category> optionalCategory = categoryRepo.findById(productRequest.getCategoryId());
            if (optionalCategory.isPresent())
                product.setCategory(optionalCategory.get());
        }
        //variant
        List<VariationRequest> variationsRequest = productRequest.getVariations();
        if (variationsRequest != null && !variationsRequest.isEmpty()) {
            for (VariationRequest v :
                    variationsRequest) {
                Variation variation;
                if (v.getId() != null && variantRepo.existsById(v.getId())) {
                    variation = variantRepo.getById(v.getId());
                } else {
                    variation = new Variation();
                    variation.setProduct(product);
                }
                variation.setPrice(v.getPrice());
                variation.setQty(v.getQty());
                //get color
                ColorRequest c = v.getColor();
                if (c != null) {
                    Color color;
                    if (c.getId() != null && colorRepo.existsById(c.getId())) {
                        color = colorRepo.getById(c.getId());
                    } else {
                        color = new Color();
                        color.setProduct(product);
                    }
                    color.setCode(c.getCode());
                    color = colorRepo.save(color);
                    variation.setColor(color);
                }
                //get size
                SizeRequest s = v.getSize();
                if (s != null) {
                    Size size;
                    if (s.getId() != null && colorRepo.existsById(s.getId())) {
                        size = sizeRepo.getById(s.getId());
                    } else {
                        size = new Size();
                        size.setProduct(product);
                        variation.setSize(size);
                    }
                    size.setSize(s.getSize());
                    size = sizeRepo.save(size);
                    variation.setSize(size);
                }
                variantRepo.save(variation);
            }
        }


        //save images
        int i = -1;
        for (Long imgId :
                productRequest.getImages()) {
            i += 1;
            ProductImgId productImgId = new ProductImgId(product.getId(), imgId);
            //if image exist and product not has images
            if (fileRepo.existsById(imgId)
                    && !productImageRepo.existsById(productImgId)) {
                ProductImage productImage = new ProductImage();
                productImage.setId(productImgId);
                productImage.setProduct(product);
                productImage.setImage(fileRepo.getById(imgId));
                productImage.setPos(i);
                //add image
                product.addImage(productImage);
                productImageRepo.save(productImage);

            }
        }
        return ResponseEntity.ok(productRequest);
    }

    public ResponseEntity<?> saveBrand(BrandRequest brandRequest) {
        if (brandRepo.existsByName(brandRequest.getName()))
            return ResponseEntity.status(409).body("Brand has already existed");
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brandRepo.save(brand);

        return ResponseEntity.ok("Save brand success");
    }

    public ResponseEntity<?> getAllBrands() {
        List<Brand> brands = brandRepo.findAll();
        List<BrandDTO> rs = brands.stream().map(BrandMapper::toBrandDTO).collect(Collectors.toList());
        return ResponseEntity.ok(rs);
    }


    public ResponseEntity<?> deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStatus(0);
            productRepo.save(product);
            return ResponseEntity.ok("Delete product success");
        }
        return ResponseEntity.notFound().build();
    }
}