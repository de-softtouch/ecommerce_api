package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.model.User;
import com.api.ecommerceweb.repository.ShopRepository;
import com.api.ecommerceweb.request.ShopRequest;
import lombok.RequiredArgsConstructor;
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

    public Shop saveShop(ShopRequest shopRequest) {
        Long shopRequestId = shopRequest.getId();


        return null;
    }
}
