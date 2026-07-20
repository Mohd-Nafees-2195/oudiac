package com.app.oudiac.dtos.brandDtos;

import com.app.oudiac.models.Brand;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandResponseDto {
    private Long id;
    private String name;
    private String code;

    public static BrandResponseDto fromBrandToBrandResponseDto(Brand brand) {
        BrandResponseDto response=new BrandResponseDto();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setCode(brand.getCode());
        return response;
    }
}
