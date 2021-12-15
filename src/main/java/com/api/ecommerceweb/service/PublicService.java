package com.api.ecommerceweb.service;

import com.api.ecommerceweb.controller.publicc.PublicController;
import com.api.ecommerceweb.helper.PublicServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicService {

    private final PublicServiceHelper publicServiceHelper;

    public ResponseEntity<?> getNewestProducts() {
        return publicServiceHelper.getNewestProducts();
    }


    public ResponseEntity<?> getProducts(Map<String,String> params) {
        return publicServiceHelper.getProducts(params);
    }

}
