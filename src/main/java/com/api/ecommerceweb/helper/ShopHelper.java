package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.reponse.BaseResponseBody;
import com.api.ecommerceweb.request.ShopRequest;
import com.api.ecommerceweb.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("ShopHelper")
@RequiredArgsConstructor
public class ShopHelper {

    private final ShopService shopService;

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
}
