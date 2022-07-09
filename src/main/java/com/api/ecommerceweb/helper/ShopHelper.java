package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.reponse.BaseResponseBody;
import com.api.ecommerceweb.reponse.ShopDetailResponse;
import com.api.ecommerceweb.reponse.ShopProductDetailResponse;
import com.api.ecommerceweb.request.ShopRequest;
import com.api.ecommerceweb.request.UpdateShopRequest;
import com.api.ecommerceweb.service.ShopService;
import com.api.ecommerceweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("ShopHelper")
@RequiredArgsConstructor
public class ShopHelper {

    private final ShopService shopService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    /*
    register shop
     */
    public ResponseEntity<?> registerShop(ShopRequest shopRequest) {
        Shop currentUserShop = shopService.getCurrentUserShop();
        if (currentUserShop == null) {
            Shop shop = new Shop();
            shop.setActive(0);
            shop.setName(shopRequest.getName());
            shop.setEmail(shopRequest.getEmail());
            shop.setDescription(shopRequest.getDescription());
            shop.setNumberPhone(shopRequest.getPhone());
            shopService.saveShop(shop);
            return ResponseEntity.ok("");
        } else {
            BaseResponseBody baseResponseBody = new BaseResponseBody();
            baseResponseBody.setMessage("Shop already existed");
            baseResponseBody.setStatusCode(422);
            baseResponseBody.setStatusText(HttpStatus.UNPROCESSABLE_ENTITY.name());
            return ResponseEntity.status(211).body(baseResponseBody);
        }

    }

    public ResponseEntity<?> getShopDetail(Long id) {
        Shop shop = shopService.getShop(id);
        int products = shop.getProducts().size();
        ShopDetailResponse shopDetailResponse = modelMapper.map(shop, ShopDetailResponse.class);
        shopDetailResponse.setProductCount(products);
        return ResponseEntity.ok(BaseResponseBody.success(shopDetailResponse, null, null));
    }

    public ResponseEntity<?> updateShop(UpdateShopRequest request) {
        Shop shop = userService.getCurrentUserShop();
        if (shop == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.notFound("Can not found your shop!"));
        }
        shop.setName(request.getName());
        shop.setStatus(request.getStatus());
        shop.setAvt(request.getAvt());
        shop.setBanner(request.getBanner());
        shop.setDescription(request.getDescription());
        userService.saveShop(shop);
        return ResponseEntity.ok(BaseResponseBody.successMessage("Update shop success!"));
    }
}
