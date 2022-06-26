package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.dto.BrandDTO;
import com.api.ecommerceweb.mapper.BrandMapper;
import com.api.ecommerceweb.model.Brand;
import com.api.ecommerceweb.reponse.BaseResponseBody;
import com.api.ecommerceweb.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("BrandHelper")
@RequiredArgsConstructor
public class BrandHelper {

    private final BrandService brandService;

    public ResponseEntity<?> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        List<BrandDTO> rs =
                brands.stream().map(BrandMapper::toBrandDTO).collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseBody.success(rs, "Get brand success!", null));
    }
}
