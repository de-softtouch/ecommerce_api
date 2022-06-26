package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Brand;
import com.api.ecommerceweb.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepo;


    public List<Brand> getAllBrands() {
        return brandRepo.findAll();

    }

    public boolean existById(Long brandReqId) {
        return brandRepo.existsById(brandReqId);
    }

    public Brand getById(Long brandReqId) {
        return brandRepo.findById(brandReqId).orElse(null);
    }

    public boolean existByName(String brandReqName) {
        return brandRepo.existsByName(brandReqName);
    }

    public Brand getByName(String brandReqName) {
        return brandRepo.findByName(brandReqName).orElse(null);
    }

    public Brand save(Brand brand) {
        return brandRepo.saveAndFlush(brand);
    }
}
