package com.api.ecommerceweb.mapper;

import com.api.ecommerceweb.dto.AddressDTO;
import com.api.ecommerceweb.model.Address;

public class AddressMapper {

    public static AddressDTO toAddressDto(Address address) {
        AddressDTO addressDto = new AddressDTO();
        addressDto.setId(address.getId());
        addressDto.setFullName(address.getFullName());
        addressDto.setPostCode(address.getPostCode());
        addressDto.setAddressDetails(address.getAddressDetails());
        addressDto.setPhone(address.getPhone());
        addressDto.setType(address.getType());
        addressDto.setIsDefault(address.getIsDefault());
        return addressDto;
    }
}
