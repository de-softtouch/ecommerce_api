package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.model.*;
import com.api.ecommerceweb.reponse.*;
import com.api.ecommerceweb.request.*;
import com.api.ecommerceweb.service.*;
import com.api.ecommerceweb.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component("ProductHelper")
@RequiredArgsConstructor
@Slf4j
public class ProductHelper {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final ProductVariantService variantService;
    private final ProductModelService productModelService;
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final VariantOptionService variantOptionService;
    private final ProductImageService productImageService;

    public ResponseEntity<?> getNewestProducts() {
        List<Product> newestProducts = productService.getNewestProducts();
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
            map.put("standardPrice", product.getMinPrice());
            map.put("salesPrice", product.getMaxPrice());
            rs.add(map);
        }
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> getProducts(Map<String, String> params) {
        String name = null;
        if (!ValidationUtil.isNullOrBlank(params.get("name"))) {
            name = "%" + params.get("name") + "%";
        }

        String category = null;
        if (!ValidationUtil.isNullOrBlank(params.get("category")))
            category = "%" + params.get("category") + "%";

        String brand = null;
        if (!ValidationUtil.isNullOrBlank(params.get("brand")))
            brand = "%" + params.get("brand") + "%";

        Integer page;
        if (ValidationUtil.isNumeric(params.get("page"))) {
            page = Integer.parseInt(params.get("page"));
        } else {
            page = 0;
        }
        Integer pageSize;
        if (ValidationUtil.isNumeric(params.get("pageSize"))) {
            pageSize = Integer.parseInt(params.get("pageSize"));
        } else {
            pageSize = 100;
        }

        Double minPrice = null;
        if (ValidationUtil.isNumeric(params.get("minPrice"))) {
            minPrice = Double.parseDouble(params.get("minPrice"));
        }
        Double maxPrice = null;
        if (ValidationUtil.isNumeric(params.get("maxPrice"))) {
            maxPrice = Double.parseDouble(params.get("maxPrice"));
        }
        String sortBy;
        if (!ValidationUtil.isNullOrBlank(params.get("sortBy"))) {
            sortBy = params.get("sortBy");
        } else {
            sortBy = "create_date";
        }

        String direction;
        if (!ValidationUtil.isNullOrBlank(params.get("direction"))) {
            direction = params.get("direction");
        } else {
            direction = "DESC";
        }
        PageRequest pageable =
                PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(direction), sortBy));
        List<Product> products =
                productService.search(name, category, brand, minPrice, maxPrice, pageable);

        List<ProductResponse> rs = new ArrayList<>();
        for (Product product :
                products) {
            int solid = productService.countSolidNumber(product);
            ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
            productResponse.setSolid(solid);
            List<ProductImage> images = productImageService.getProductImages(product, 1);
            productResponse.setImages(images.stream()
                    .map(i -> modelMapper.map(i, ProductImageResponse.class))
                    .collect(Collectors.toList()));
            rs.add(productResponse);
        }
        return ResponseEntity.ok(BaseResponseBody.success(rs, "Fetch products success!", "Fetch products success!"));
    }

    /*
    get product detail
     */
    public ResponseEntity<?> getProductDetail(Long id) {
        Product product = productService.getProduct(id);


        if (product != null) {
            ProductDetailResponse productResponse = modelMapper.map(product, ProductDetailResponse.class);
            //get product variants
            List<Variant> variants = variantService.getVariants(product);
            List<VariantResponse> variantResponses = variants.stream()
                    .map(variant -> {
                        VariantResponse variantResponse = modelMapper.map(variant, VariantResponse.class);
                        List<String> options = variant.getOptions().stream().map(
                                VariantOption::getName
                        ).collect(Collectors.toList());
                        variantResponse.setOptions(options);
                        variantResponse.setImages(variant.getImages().stream()
                                .map(FileUpload::getName)
                                .collect(Collectors.toCollection(LinkedList::new)));
                        return variantResponse;
                    })
                    .collect(Collectors.toList());
            productResponse.setVariants(variantResponses);
            //get product model
            List<ProductModel> productModels = productModelService.getProductModels(product, 1);
            List<ProductModelResponse> productModelResponses = productModels.stream()
                    .map(model -> {
                        ProductModelResponse modelResponse = modelMapper.map(model, ProductModelResponse.class);
                        List<VariantOption> variantOptions = model.getVariantOptions();
                        for (VariantOption variantOption :
                                variantOptions) {
                            modelResponse.getVariantOptionIndexes().add(variantOption.getId());
                        }
                        return modelResponse;
                    })
                    .collect(Collectors.toCollection(LinkedList::new));
            productResponse.setModels(productModelResponses);
            //get images
            List<ProductImage> productImages = productImageService.getProductImages(product, 1);
            productResponse.setImages(
                    productImages.stream()
                            .map(productImage -> modelMapper.map(productImage, ProductImageResponse.class))
                            .collect(Collectors.toList())
            );
            return ResponseEntity.ok(
                    BaseResponseBody.success(productResponse, "Get product by id : " + id, null)
            );
        }
        return ResponseEntity.ok(
                BaseResponseBody.notFound("Could not found product with id: " + id)
        );
    }

    public ResponseEntity<?> getShopProductDetail(Long id) {
        Product product = productService.getProduct(id);


        if (product != null) {
            ShopProductDetailResponse productResponse = modelMapper.map(product, ShopProductDetailResponse.class);
            //get product variants
            List<Variant> variants = variantService.getVariants(product);
            List<ShopVariantResponse> variantResponses = variants.stream()
                    .map(variant -> {
                        ShopVariantResponse variantResponse = modelMapper.map(variant, ShopVariantResponse.class);
                        variantResponse.setOptions(
                                variant.getOptions()
                                        .stream()
                                        .map(o -> modelMapper.map(o, VariantOptionResponse.class))
                                        .collect(Collectors.toList())
                        );
                        variantResponse.setImages(variant.getImages().stream()
                                .map(FileUpload::getName)
                                .collect(Collectors.toCollection(LinkedList::new)));
                        return variantResponse;
                    })
                    .collect(Collectors.toList());
            productResponse.setVariants(variantResponses);
            //get product model
            List<ProductModel> productModels = productModelService.getProductModels(product, 1);
            List<ProductModelResponse> productModelResponses = productModels.stream()
                    .map(model -> {
                        ProductModelResponse modelResponse = modelMapper.map(model, ProductModelResponse.class);
                        List<VariantOption> variantOptions = model.getVariantOptions();
                        for (VariantOption variantOption :
                                variantOptions) {
                            modelResponse.getVariantOptionIndexes().add(variantOption.getId());
                        }
                        return modelResponse;
                    })
                    .collect(Collectors.toCollection(LinkedList::new));
            productResponse.setModels(productModelResponses);
            //get images
            List<ProductImage> productImages = product.getImages();
            productResponse.setImages(
                    productImages.stream()
                            .map(productImage -> modelMapper.map(productImage, ProductImageResponse.class))
                            .collect(Collectors.toList())
            );
            return ResponseEntity.ok(
                    BaseResponseBody.success(productResponse, "Get product by id : " + id, null)
            );
        }
        return ResponseEntity.ok(
                BaseResponseBody.notFound("Could not found product with id: " + id)
        );
    }

    public ResponseEntity<?> saveProduct(ProductRequest productRequest) {
        User user = userService.getCurrentUser();
        Long productReqId = productRequest.getId();
        //get current user shop
        Shop shop = user.getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().body("User shop not exist!");
        }
        Product product = null;
        if (productReqId != null && productService.existInShop(shop) && (product = productService.getById(productReqId)) != null) {
        } else {
            product = new Product();
        }
        product.setStatus(productRequest.getStatus());
        product.setName(productRequest.getName());
        product.setMinPrice(productRequest.getMinPrice());
        product.setMaxPrice(productRequest.getMaxPrice());
        product.setDescription(productRequest.getDescription());
        product.setStock(productRequest.getStock());
        product.setStandardPrice(productRequest.getStandardPrice());
        product.setSalesPrice(productRequest.getSalesPrice());
        product.setActive(productRequest.getActive());
        product.setStatus(productRequest.getStatus());
        product.setShop(shop);

        Long coverImgId = productRequest.getCoverImage();
        if (coverImgId != null) {
            FileUpload coverImage = fileUploadService.getFileById(coverImgId);
            if (coverImage != null) {
                product.setCoverImage(coverImage);
            }
        }


        product = productService.saveProduct(product);

        BrandRequest brandRequest = productRequest.getBrand();
        if (brandRequest != null) {
            Long brandReqId = brandRequest.getId();
            if (brandReqId != null
                    && brandService.existById(brandReqId)) {
                product.setBrand(brandService.getById(brandReqId));
            } else {
                String brandReqName = brandRequest.getName();
                if (brandReqName != null) {
                    if (brandService.existByName(brandReqName)) {
                        Brand brand = brandService.getByName(brandReqName);
                        product.setBrand(brand);
                    } else {
                        Brand brand = new Brand();
                        brand.setName(brandReqName);
                        brand = brandService.save(brand);
                        product.setBrand(brand);
                    }
                }
            }
        } else {
            //set un category
        }
        Long categoryReqId = productRequest.getCategoryId();
        if (categoryReqId != null) {
            Category category = categoryService.getCategory(categoryReqId);
            if (category != null) {
                product.setCategory(category);
            }
        } else {
            //set un brand
            product.setCategory(categoryService.getByName("uncategory"));
        }
        product = productService.saveProduct(product);
        //
        Set<Long> images = productRequest.getImages();
        for (Long id :
                images) {
            FileUpload fileUpload = fileUploadService.getFileById(id);
            ProductImage productImage = new ProductImage();
            if (productImageService.getProductImage(product, fileUpload) == null) {
                productImage.setId(new ProductImgId(product.getId(), fileUpload.getId()));
                productImage.setProduct(product);
                productImage.setImage(fileUpload);
                productImage.setStatus(1);
                log.info("save product image");
                productImageService.save(productImage);
            }
        }
        //variants
        List<VariantRequest> variantRequests = productRequest.getVariants();
        for (VariantRequest variantRequest : variantRequests) {
            Long vReqId = variantRequest.getId();
            String variantReqName = variantRequest.getName();
            Variant variant;
            if (((vReqId != null && (variant = variantService.getByIdAndProduct(vReqId, product)) != null))
                    || ((variantReqName != null && (variant = variantService.getByNameAndProduct(variantReqName, product)) != null))) {
                //update variant
                variant.setStatus(variantRequest.getStatus());
            } else {
                variant = new Variant();
                variant.setStatus(1);
            }
            variant.setName(variantReqName);
            variant = variantService.save(variant);
            //option
            List<VariantOptionRequest> variantOptionRequests = variantRequest.getOptions();
            for (VariantOptionRequest variantOptionReq :
                    variantOptionRequests) {
                VariantOption variantOption;
                Long variantOptReqId = variantOptionReq.getId();
                String variantOptionReqName = variantOptionReq.getName();
                if ((variantOptReqId != null
                        && (variantOption = variantOptionService.getByIdAndProduct(variantOptReqId, product)) != null
                        && variantService.existByOption(variantOption))
                        || ((variantOptionReqName != null
                        && (variantOption = variantOptionService.getByNameAndProduct(variantOptionReqName, product)) != null))) {
                    variantOption.setStatus(variantOptionReq.getStatus());
                    //option da ton tai
                } else {
                    //option chua ton tai
                    variantOption = new VariantOption();
                    variant.getOptions().add(variantOption);
                    variantOption.setVariant(variant);
                    variantOption.setStatus(1);
                }
                variantOption.setName(variantOptionReqName);
                variant.setProduct(product);
                variantOptionService.save(variantOption);
            }
        }
        Converter<VariantOption, String> converter = mappingContext -> {
            VariantOption source = mappingContext.getSource();
            return source.getName();
        };
        //
        if (modelMapper.getTypeMap(VariantOption.class, String.class) == null)
            modelMapper.createTypeMap(VariantOption.class, String.class).setConverter(converter);
        //save product model
        List<ProductModelRequest> modelRequests = productRequest.getModels();
        for (ProductModelRequest modelReq :
                modelRequests) {
            ProductModel productModel;
            Long modelReqId = modelReq.getId();
            //variants
            String modelReqName = modelReq.getName();
            List<Long> modelOptionIndexes = modelReq.getVariantOptionIndexes();
            //
            String modelName = "";
            List<VariantOption> variantOptions = new ArrayList<>();

            if (modelReqName.contains(",")) {
                String[] split = modelReqName.split(",");
                VariantOption v1 = variantOptionService.getByNameAndProduct(split[0], product);
                VariantOption v2 = variantOptionService.getByNameAndProduct(split[1], product);
                if (v1 == null || v2 == null)
                    continue;
                modelName = v1.getName() + "," + v2.getName();
                variantOptions.addAll(Arrays.asList(v1, v2));
            } else {
                VariantOption v1 = variantOptionService.getByNameAndProduct(modelReqName, product);
                if (v1 == null)
                    continue;
                modelName = v1.getName();
                variantOptions.add(v1);
            }
            //get model by id model request
            if (modelReq.getId() != null
                    && (productModel = productModelService.getByIdAndProduct(modelReqId, product)) != null
                    || (modelReqName != null && (productModel = productModelService.getByNameAndProduct(modelReqName, product)) != null)) {
                productModel.setStatus(modelReq.getStatus());
            } else {
                //create new product model
                productModel = new ProductModel();
                productModel.setProduct(product);
                productModel.setStatus(1);
                product.getModels().add(productModel);
            }

            productModel.setName(modelName);
            productModel.setVariantOptions(variantOptions);
            productModel.setStock(modelReq.getStock());
            productModel.setPrice(modelReq.getPrice());
            Double priceBeforeDiscount = modelReq.getPriceBeforeDiscount();
            productModel.setPriceBeforeDiscount(priceBeforeDiscount != null ? priceBeforeDiscount : modelReq.getPrice());
            productModel = productModelService.save(productModel);
        }
        //remove
//        Set<Long> removedOptions = productRequest.getRemovedOptions();
//        if (!removedOptions.isEmpty()) {
//            removedOptions.forEach(aLong -> {
//                VariantOption variantOption = variantOptionService.getById(aLong);
//                if (variantOption != null) {
//                    List<ProductModel> productModels = productModelService.getAllByVariantOption(variantOption);
//                    productModels.forEach(p -> {
//                        p.setStatus(0);
//                        productModelService.save(p);
//                    });
//                    variantOption.getmodel
//                    variantOption.setStatus(0);
//                    variantOptionService.save(variantOption);
//                }
//            });
//        }

        //
        ProductDetailResponse productResponse = modelMapper.map(product, ProductDetailResponse.class);
        List<Variant> variants = variantService.getVariants(product);
        List<VariantResponse> variantResponses = variants.stream()
                .map(v -> modelMapper.map(v, VariantResponse.class))
                .collect(Collectors.toList());
        productResponse.setVariants(variantResponses);

        //get product model active
        List<ProductModel> productModels = productModelService.getProductModels(product, 1);
        List<ProductModelResponse> productModelResponses = productModels.stream()
                .map(model -> {
                    ProductModelResponse modelResponse = modelMapper.map(model, ProductModelResponse.class);
                    List<VariantOption> variantOptions = model.getVariantOptions();
                    for (VariantOption variantOption :
                            variantOptions) {
                        modelResponse.getVariantOptionIndexes().add(variantOption.getId());
                    }
                    return modelResponse;
                })
                .collect(Collectors.toCollection(LinkedList::new));
        productResponse.setModels(productModelResponses);

        return ResponseEntity.ok(productResponse);
    }

    public ResponseEntity<?> updateProduct(ProductRequest productRequest) {
        User user = userService.getCurrentUser();
        Long productReqId = productRequest.getId();
        //get current user shop
        Shop shop = user.getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().body("User shop not exist!");
        }
        Product product = null;
        if (productReqId != null && productService.existInShop(shop) && (product = productService.getById(productReqId)) != null) {
        } else {
            product = new Product();
        }
        product.setStatus(productRequest.getStatus());
        product.setName(productRequest.getName());
        product.setMinPrice(productRequest.getMinPrice());
        product.setMaxPrice(productRequest.getMaxPrice());
        product.setDescription(productRequest.getDescription());
        product.setStock(productRequest.getStock());
        product.setStandardPrice(productRequest.getStandardPrice());
        product.setSalesPrice(productRequest.getSalesPrice());
        product.setActive(productRequest.getActive());
        product.setStatus(productRequest.getStatus());
        product.setShop(shop);

        Long coverImgId = productRequest.getCoverImage();
        if (coverImgId != null) {
            FileUpload coverImage = fileUploadService.getFileById(coverImgId);
            if (coverImage != null) {
                product.setCoverImage(coverImage);
            }
        }


        product = productService.saveProduct(product);

        BrandRequest brandRequest = productRequest.getBrand();
        if (brandRequest != null) {
            Long brandReqId = brandRequest.getId();
            if (brandReqId != null
                    && brandService.existById(brandReqId)) {
                product.setBrand(brandService.getById(brandReqId));
            } else {
                String brandReqName = brandRequest.getName();
                if (brandReqName != null) {
                    if (brandService.existByName(brandReqName)) {
                        Brand brand = brandService.getByName(brandReqName);
                        product.setBrand(brand);
                    } else {
                        Brand brand = new Brand();
                        brand.setName(brandReqName);
                        brand = brandService.save(brand);
                        product.setBrand(brand);
                    }
                }
            }
        } else {
            //set un category
        }
        Long categoryReqId = productRequest.getCategoryId();
        if (categoryReqId != null) {
            Category category = categoryService.getCategory(categoryReqId);
            if (category != null) {
                product.setCategory(category);
            }
        } else {
            //set un brand
            product.setCategory(categoryService.getByName("uncategory"));
        }
        product = productService.saveProduct(product);
        //variants
        List<VariantRequest> variantRequests = productRequest.getVariants();
        for (VariantRequest variantRequest : variantRequests) {
            Long vReqId = variantRequest.getId();
            String variantReqName = variantRequest.getName();
            Variant variant;
            if (((vReqId != null && (variant = variantService.getByIdAndProduct(vReqId, product)) != null))
                    || ((variantReqName != null && (variant = variantService.getByNameAndProduct(variantReqName, product)) != null))) {
                //update variant
                variant.setStatus(variantRequest.getStatus());
            } else {
                variant = new Variant();
                variant.setStatus(1);
            }
            variant.setName(variantReqName);
            variant = variantService.save(variant);
            //option
            List<VariantOptionRequest> variantOptionRequests = variantRequest.getOptions();
            for (VariantOptionRequest variantOptionReq :
                    variantOptionRequests) {
                VariantOption variantOption;
                Long variantOptReqId = variantOptionReq.getId();
                String variantOptionReqName = variantOptionReq.getName();
                if ((variantOptReqId != null
                        && (variantOption = variantOptionService.getByIdAndProduct(variantOptReqId, product)) != null
                        && variantService.existByOption(variantOption))
                        || ((variantOptionReqName != null
                        && (variantOption = variantOptionService.getByNameAndProduct(variantOptionReqName, product)) != null))) {
                    variantOption.setStatus(variantOptionReq.getStatus());
                    //option da ton tai
                } else {
                    //option chua ton tai
                    variantOption = new VariantOption();
                    variant.getOptions().add(variantOption);
                    variantOption.setVariant(variant);
                    variantOption.setStatus(1);
                }
                variantOption.setName(variantOptionReqName);
                variant.setProduct(product);
                variantOptionService.save(variantOption);
            }
        }
        Converter<VariantOption, String> converter = mappingContext -> {
            VariantOption source = mappingContext.getSource();
            return source.getName();
        };
        //
        if (modelMapper.getTypeMap(VariantOption.class, String.class) == null)
            modelMapper.createTypeMap(VariantOption.class, String.class).setConverter(converter);
        //save product model

        List<ProductModelRequest> modelRequests = productRequest.getModels();


        //
        ProductDetailResponse productResponse = modelMapper.map(product, ProductDetailResponse.class);
        List<Variant> variants = variantService.getVariants(product);
        List<VariantResponse> variantResponses = variants.stream()
                .map(v -> modelMapper.map(v, VariantResponse.class))
                .collect(Collectors.toList());
        productResponse.setVariants(variantResponses);
        //get product model active
        List<ProductModel> productModels = productModelService.getProductModels(product, 1);
        List<ProductModelResponse> productModelResponses = productModels.stream()
                .map(model -> {
                    ProductModelResponse modelResponse = modelMapper.map(model, ProductModelResponse.class);
                    List<VariantOption> variantOptions = model.getVariantOptions();
                    for (VariantOption variantOption :
                            variantOptions) {
                        modelResponse.getVariantOptionIndexes().add(variantOption.getId());
                    }
                    return modelResponse;
                })
                .collect(Collectors.toCollection(LinkedList::new));
        productResponse.setModels(productModelResponses);

        return ResponseEntity.ok(productResponse);
    }

    public ResponseEntity<?> getPurchaseQuantities(Long modelId, Long productId, Long shopId, Integer quantity) {
        ProductModel productModel = productModelService.getPurcharQuantity(productId, modelId, shopId, quantity);
        if (productModel == null)
            return ResponseEntity.badRequest().build();
        ProductPurchaseQuantityResponse resp = new ProductPurchaseQuantityResponse();
        resp.setModelId(productModel.getId());
        resp.setStock(productModel.getStock());
        resp.setShopId(shopId);
        resp.setModelName(productModel.getName());
        resp.setProductId(productId);
        resp.setSalesPrice(productModel.getPrice());
        resp.setStandardPrice(productModel.getPriceBeforeDiscount());
        resp.setMaxPurchaseQuantity(productModel.getMaxPurchaseQuantity());
        return ResponseEntity.ok(BaseResponseBody.success(resp, "get purchase quantity success", "success"));
    }

    public ResponseEntity<?> getProductsInShop(Map<String, String> params) {
        String statusParam = params.get("statusParam");
        Integer status = 1;
        if (ValidationUtil.isNumeric(statusParam))
            status = Integer.parseInt(statusParam);

        BaseParamRequest baseParamRequest = new BaseParamRequest(params);
        Pageable pageable = baseParamRequest.toPageRequest();
        Shop shop = userService.getCurrentUserShop();
        List<Product> products = productService.getProductsInShop(shop.getId(), status, pageable);

        List<ProductResponse> data = products.stream()
                .map(p -> {
                    ProductResponse productResponse = modelMapper.map(p, ProductResponse.class);
                    //
                    productResponse.setImages(
                            productImageService.getProductImages(p, 1).stream()
                                    .map(i -> modelMapper.map(i, ProductImageResponse.class))
                                    .collect(Collectors.toList())
                    );
                    //product models
                    List<ProductModel> productModels = productModelService.getProductModels(p, 1);

                    //
                    //
                    AtomicReference<Integer> availableStock = new AtomicReference<>(0);
                    List<ProductModelResponse> productModelResponses = productModels.stream()
                            .map(model -> {
                                availableStock.updateAndGet(v -> v + model.getStock());
                                ProductModelResponse modelResponse = modelMapper.map(model, ProductModelResponse.class);
                                List<VariantOption> variantOptions = model.getVariantOptions();
                                for (VariantOption variantOption :
                                        variantOptions) {
                                    modelResponse.getVariantOptionIndexes().add(variantOption.getId());
                                }
                                return modelResponse;
                            })
                            .collect(Collectors.toCollection(LinkedList::new));
                    productResponse.setSolid(productService.countSolidNumber(p));
                    if (!productModels.isEmpty()) {
                        productResponse.setModels(productModelResponses);
                        productResponse.setStock(availableStock.get());
                    } else {
                        productResponse.setStock(p.getStock());

                    }
                    return productResponse;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseBody.success(data, "get products success", "success"));
    }
}
