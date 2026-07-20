package com.app.oudiac.dtos.productTypeDtos;

import com.app.oudiac.models.ProductType;
import lombok.Data;

import java.util.Date;

@Data
public class ProductTypeResponseDto {
    private Long id;
    private String name;
    private String code;
    private Date created_at;
    private Date updated_at;

    public static ProductTypeResponseDto fromProductType(ProductType productType){
        ProductTypeResponseDto response=new ProductTypeResponseDto();

        response.setId(productType.getId());
        response.setName(productType.getName());
        response.setCode(productType.getCode());
        response.setCreated_at(productType.getCreated_at());
        response.setUpdated_at(productType.getUpdated_at());

        return response;
    }
}
