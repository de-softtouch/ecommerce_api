package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.model.User;
import com.api.ecommerceweb.repository.ShopRepository;
import com.api.ecommerceweb.request.ShopRequest;
import com.api.ecommerceweb.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepo;

    public Shop getShop(User user) {
        Optional<Shop> optionalShop = shopRepo.findByOwners(user);
        if (optionalShop.isPresent()) {
            return optionalShop.get();
        }
        return null;
    }


    public Shop getCurrentUserShop(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getShop();
    }
    public void changeStatus(){

    }

    public Shop getShop(Long id){
        return shopRepo.findById(id).orElse(null);
    }



    public void saveShop(Shop shop) {
         shopRepo.save(shop);
    }
}

