package com.api.ecommerceweb.service;

import com.api.ecommerceweb.dto.BrandDTO;
import com.api.ecommerceweb.enumm.ERole;
import com.api.ecommerceweb.mapper.BrandMapper;
import com.api.ecommerceweb.model.*;
import com.api.ecommerceweb.repository.*;
import com.api.ecommerceweb.request.*;
import com.api.ecommerceweb.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("SellerHelper")
@RequiredArgsConstructor
@Slf4j
public class SellerService {

    private final ProductRepository productRepo;
    private final BrandRepository brandRepo;
    private final ProductImageRepository productImageRepo;
    private final FileRepository fileRepo;
    private final CategoryRepository categoryRepo;
    private final OrderRepository orderRepo;
    private final ShopRepository shopRepo;
    private final FeedbackRepository feedbackRepo;
    private final UserRepository userRepo;
    private final OrderItemRepository orderItemRepo;
    private final RoleRepository roleRepo;

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
//        map.put("id", product.getId());
//        map.put("name", product.getName());
//        map.put("standardPrice", product.getMinPrice());
//        map.put("salesPrice", product.getMaxPrice());
//        //brand
//        if (product.getBrand() != null) {
//            Map<String, Object> brand = new HashMap<>();
//            brand.put("id", product.getBrand().getId());
//            brand.put("name", product.getBrand().getName());
//            map.put("brand", brand);
//        }
//        //category
//        if (product.getCategory() != null) {
//            Map<String, Object> category = new HashMap<>();
//            category.put("id", product.getCategory().getId());
//            category.put("name", product.getCategory().getName());
//            map.put("category", category);
//        }
//        //product images
//        map.put("images", product.getImages() != null ?
//                product.getImages().stream()
//                        .map(productImage -> Map.of(
//                                "pos", productImage.getPos(),
//                                "name", productImage.getImage().getName(),
//                                "id", productImage.getId(),
//                                "type", productImage.getImage().getType()
//                        ))
//                        .collect(Collectors.toList()) : new ArrayList<>());
//        //group variants
//        Set<Color> colors = product.getColors();
//        if (colors != null && !colors.isEmpty()) {
//            map.put("colors", colors.stream().map(Color::getCode).collect(Collectors.toList()));
//        }
//        Set<Size> sizes = product.getSizes();
//        if (sizes != null && !sizes.isEmpty()) {
//            map.put("sizes", sizes.stream().map(Size::getSize).collect(Collectors.toList()));
//        }
//        //product variations
//        List<Object> vResponses = new ArrayList<>();
//        for (Variation variation :
//                product.getVariations()) {
//            HashMap<String, Object> v = new HashMap<>();
//            v.put("id", variation.getId());
//            v.put("price", variation.getStandardPrice());
//            v.put("qty", 12);
//            //color
//            Map<String, Object> color = new HashMap<>();
//            color.put("id", variation.getColor().getId());
//            color.put("code", variation.getColor().getCode());
//            v.put("color", color);
//            //size
//            Map<String, Object> size = new HashMap<>();
//            size.put("id", variation.getSize().getId());
//            size.put("size", variation.getSize().getSize());
//            v.put("size", size);
//            vResponses.add(v);
//        }
//        map.put("variations", vResponses);
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
        product.setMinPrice(productRequest.getMinPrice());
        product.setMaxPrice(productRequest.getMaxPrice());
        product.setDescription(productRequest.getDescription());
        //images and video

        if (productRequest.getCoverVideo() != null) {
            Optional<FileUpload> optionalCoverVid = fileRepo.findByName(productRequest.getCoverVideo());
            if (optionalCoverVid.isPresent()) {
                product.setCoverVideo(optionalCoverVid.get());
            }
        }


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
        List<VariantRequest> variationsRequest = productRequest.getVariants();
        //save images
        int i = -1;
//        for (String imageName :
//                productRequest.getImages()) {
//            Optional<File> optionalFile = fileRepo.findByName(imageName);
//            if (optionalFile.isPresent()) {
//                File image = optionalFile.get();
//                Long imgId = image.getId();
//                i += 1;
//                ProductImgId productImgId = new ProductImgId(product.getId(), imgId);
//                //if image exist and product not has images
//                if (!productImageRepo.existsById(productImgId)) {
//                    ProductImage productImage = new ProductImage();
//                    productImage.setId(productImgId);
//                    productImage.setProduct(product);
//                    productImage.setImage(fileRepo.getById(imgId));
//                    productImage.setPos(i);
//                    //add image
//                    product.addImage(productImage);
//                    productImageRepo.save(productImage);
//
//                }
//            }

//        }
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

    public ResponseEntity<?> getOrders() {
        //TODO : sua null thanh user
        List<Object> rs = new ArrayList<>();
        List<Order> orders = orderRepo.findDistinctByItemsProductShop(null);
        for (Order order :
                orders) {
            Map<String, Object> o = toOrderDetailsResponse(order);
            rs.add(o);
        }
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> getOrders2(Map<String,String> params) {
//        List<OrderItem> orderItems = orderItemRepo.findAllByProduct_Shop(getCurrentUser().getShop());
//        List<Object> rs = new ArrayList<>();
//        for (OrderItem orderItem :
//                orderItems) {
//            Map<String, Object> item = new HashMap<>();
//            item.put("id", orderItem.getId());
//            item.put("productId", orderItem.getProduct().getId());
//            item.put("name", orderItem.getProduct().getName());
//            item.put("qty", orderItem.getQty());
//            item.put("message", orderItem.getMessage());
//            item.put("createDate", orderItem.getCreateDate());
//            //variant
//            HashMap<String, Object> variant = new HashMap<>();
//            variant.put("id", orderItem.getVariation().getId());
//            variant.put("size", orderItem.getVariation().getSize().getSize());
//            variant.put("color", orderItem.getVariation().getColor().getLabel());
//            item.put("variant", variant);
//            rs.add(item);
//        }
//        return ResponseEntity.ok(rs);
        BaseParamRequest p = new BaseParamRequest(params);

        Pageable pageable = p.toPageRequest();
        if(ValidationUtil.isNullOrBlank(params.get("status"))){
            p.setStatus(null);
        }
//        Sort sort = Sort.by(Sort.Direction.fromString(p.getDirection()), p.getSortBy(),"status");
        List<Order> orders = orderRepo.getOrdersNative(p.getStatus(),pageable);
        List<Object> rs = new ArrayList<>();
        for (Order o : orders) {
            Map<String, Object> orderResponse = toOrderDetailsResponse(o);
            rs.add(orderResponse);
        }
        return ResponseEntity.ok(rs);
    }


    private Map<String, Object> toOrderDetailsResponse(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("orderDate", order.getCreateDate());
        map.put("status", order.getStatus());
        //user
        User orderUser = order.getUser();
        Map<String, Object> u = new HashMap<>();
        u.put("id", orderUser.getId());
        u.put("username", orderUser.getFullName());
        //address
        Address address = order.getAddress();
        Map<String, Object> a = new HashMap<>();
        a.put("id", address.getId());
        a.put("addressDetails", address.getAddressDetails());
        a.put("type", address.getType());
        a.put("postCode", address.getPostCode());
        a.put("phone", address.getPhone());
        u.put("address", a);
        map.put("user", u);

        //items
        List<Object> orderItems = new ArrayList<>();
        for (OrderItem item :
                order.getItems()) {
            Map<String, Object> o = new HashMap<>();
            o.put("qty", item.getQty());
            o.put("message", item.getMessage());
            o.put("productId", item.getProduct().getId());
            o.put("name", item.getProduct().getName());
            orderItems.add(o);
        }
        map.put("orderItems", orderItems);
        return map;
    }


    public ResponseEntity<?> updateOrder(Long id, Integer status) {
//        Optional<Order> optionalOrder = orderRepo.findByIdAndShops(id, getCurrentUser().getShop());
//        if (optionalOrder.isPresent()) {
//            Optional<OrderStatus> orderStatus = OrderStatus.fromCode(status);
//            if (orderStatus.isPresent()) {
//                Order order = optionalOrder.get();
//                order.setStatus(orderStatus.get());
//                orderRepo.save(order);
//
//                return ResponseEntity.ok("Update order success");
//            }
//
//            return ResponseEntity.badRequest().body("Order status not valid");
//        }
//        return ResponseEntity.notFound().build();
        return null;
    }

    public ResponseEntity<?> getFeedbacks() {
        //TODO : sua null thanh user

        List<Feedback> feedbacks = feedbackRepo.findAllByProductShopOrderByCreateDate(null);
        List<Object> rs = new ArrayList<>();
        for (Feedback f :
                feedbacks) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", f.getId());
            map.put("rating", f.getRating());
            map.put("comment", f.getComment());
            map.put("createDate", f.getCreateDate());
            map.put("user", Map.of(
                    "id", f.getUser().getId(),
                    "fullName", f.getUser().getFullName(),
                    "avt", f.getUser().getProfileImg()));
            map.put("product", Map.of(
                    "id", f.getProduct().getId(),
                    "name", f.getProduct().getName(),
                    "img", f.getProduct().getImages().get(0).getImage().getName()));
            rs.add(map);
        }
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> getOrder(Long id) {
        //TODO : sua null thanh user

        Optional<Order> order = orderRepo.getShopOrderNative(null, id);
        if(order.isEmpty())
            return ResponseEntity.badRequest().body("Could not find order with id: "+ id);
//
//        Optional<OrderItem> optionalOrderItem = orderItemRepo.findById(id);
//        if (optionalOrderItem.isEmpty())
//            return ResponseEntity.notFound().build();
//        OrderItem orderItem = optionalOrderItem.get();
//        Map<String, Object> rs = new HashMap<>();
//        rs.put("id", orderItem.getId());
//        rs.put("message", orderItem.getMessage());
//        rs.put("createDate", orderItem.getCreateDate());
//        rs.put("qty", orderItem.getQty());
//        //variant
//        HashMap<Object, Object> variant = new HashMap<>();
//        variant.put("color", orderItem.getVariation().getColor().getLabel());
//        variant.put("size", orderItem.getVariation().getSize().getSize());
//        rs.put("variant", variant);
//        //product
//        HashMap<Object, Object> product = new HashMap<>();
//        product.put("id", orderItem.getProduct().getId());
//        product.put("name", orderItem.getProduct().getName());
//        rs.put("product", product);
        return ResponseEntity.ok(order.get().getId());


    }

    public Object getShopDetail() {
        //TODO : sua null thanh user

        User user = null;
        Shop shop = getUserShop();
        if (shop == null) {
            return null;
        }
        Map<String, Object> rs = new HashMap<>();
        rs.put("id", shop.getId());
        rs.put("name", shop.getName());
        rs.put("phone", user.getPhone());
        rs.put("email", user.getEmail());
        rs.put("description", shop.getDescription());
        if (shop.getAddress() != null) {
            Address address = shop.getAddress();
            rs.put("address",
                    Map.of("id", address.getId(),
                            "phone", address.getPhone(), "fullName", address.getFullName()));

        }
        return rs;
    }

    public Shop getUserShop() {
        //TODO : sua null thanh user

        Optional<Shop> optionalShop = shopRepo.findByOwners(null);
        if (optionalShop.isEmpty()) {
            return null;
        }
        Shop shop = optionalShop.get();
        return shop;
    }

    public void updateShop(ShopRequest shopRequest) {
        Shop shop = null;
        //TODO : sua null thanh user
        User user = null;

        if (getUserShop() == null) {
            shop = new Shop();
            user.getRoles().add(roleRepo.getByName(ERole.ROLE_SELLER));
        } else {
            shop = getUserShop();

        }
        shop.setName(shopRequest.getName());
        shop.setDescription(shopRequest.getDescription());
        shop = shopRepo.save(shop);
        user.setShop(shop);
        shop.getOwners().add(user);
        userRepo.save(user);
    }
}
