package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Address;
import com.api.ecommerceweb.repository.AddressRepository;
import com.api.ecommerceweb.request.AddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepo;


    public Address saveAddress(AddressRequest addressRequest) {
        Long addressRequestId = addressRequest.getId();
        Address address;
        if (addressRequestId != null && addressRepo.existsById(addressRequestId)) {
            address = addressRepo.getById(addressRequestId);
        } else {
            address = new Address();
        }
        address.setPhone(addressRequest.getPhone());
        address.setAddressDetails(addressRequest.getAddressDetails());
        address.setPostCode(addressRequest.getPostCode());
        return addressRepo.save(address);
    }

    public Address getById(Long addressId) {
        return addressRepo.findById(addressId).orElse(null);
    }
}
