package com.app.oudiac.dtos.productTypeDtos;

import com.app.oudiac.models.ProductType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class ProductTypeRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "sku-code is required")
    private String code;

    public static ProductType toProductType(ProductTypeRequestDto request){
        ProductType newProductType=new ProductType();
        newProductType.setName(request.getName());
        newProductType.setCode(request.getCode().toUpperCase());

        newProductType.setCreated_at(new Date());
        newProductType.setUpdated_at(new Date());
        newProductType.setIsDeleted(false);

        return newProductType;
    }
}
