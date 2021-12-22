package com.api.ecommerceweb.service;

import com.api.ecommerceweb.enumm.ERole;
import com.api.ecommerceweb.model.Address;
import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.model.User;
import com.api.ecommerceweb.reponse.UserDetail;
import com.api.ecommerceweb.repository.AddressRepository;
import com.api.ecommerceweb.repository.RoleRepository;
import com.api.ecommerceweb.repository.ShopRepository;
import com.api.ecommerceweb.repository.UserRepository;
import com.api.ecommerceweb.request.AddressRequest;
import com.api.ecommerceweb.request.ShopRequest;
import com.api.ecommerceweb.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final AddressRepository addressRepo;
    private final RoleRepository roleRepo;
    private final ShopRepository shopRepo;

    public User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

    public Shop saveShop(ShopRequest shopRequest) {
        Long shopRequestId = shopRequest.getId();
        User user = getCurrentUser();
        Shop shop;
        if (shopRequestId != null && shopRepo.existsById(shopRequestId)) {
            shop = shopRepo.getById(shopRequestId);
        } else {
            shop = new Shop();
            user.getRoles().add(roleRepo.getByName(ERole.ROLE_SELLER));
        }
        if (shopRequest.getAddress() != null) {
            Address address = saveAddress(shopRequest.getAddress());
            address.setShop(shop);
            shop.setAddress(address);
        }
        shop.setName(shopRequest.getName());
        shop.setDescription(shopRequest.getDescription());
        shop.getOwners().add(user);
        shop = shopRepo.save(shop);
        user.setShop(shop);
        userRepo.save(user);
        return shop;
    }

    public Address saveAddress(AddressRequest addressRequest) {
        Address address;
        if (addressRequest.getId() != null && addressRepo.existsById(addressRequest.getId())) {
            address = addressRepo.getById(addressRequest.getId());
        } else {
            address = new Address();
        }
        address.setPhone(addressRequest.getPhone());
        address.setFullName(addressRequest.getFullName());
        address.setAddressDetails(addressRequest.getAddressDetails());
        address.setPostCode(addressRequest.getPostCode());
        address.setUser(getCurrentUser());
        address = addressRepo.save(address);
        return address;
    }



}
