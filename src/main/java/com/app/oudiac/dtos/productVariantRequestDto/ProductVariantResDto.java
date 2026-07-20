package com.app.oudiac.dtos.productVariantRequestDto;

import com.app.oudiac.models.ProductVariant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductVariantResDto {
    private Long id;
    private String variantType;  //100ml, 50 ml
    private BigDecimal sellingPrice;
    private BigDecimal MRP;
    private String sku;
    private Long stock;
    private Date created_at;
    private Date updated_at;

    public static ProductVariantResDto fromProductVariant(ProductVariant productVariant){
        ProductVariantResDto response=new ProductVariantResDto();
        response.setId(productVariant.getId());
        response.setVariantType(productVariant.getVariantType());
        response.setSellingPrice(productVariant.getSellingPrice());
        response.setMRP(productVariant.getMRP());
        response.setSku(productVariant.getSku());
        response.setStock(productVariant.getStock());
        response.setCreated_at(productVariant.getCreated_at());
        response.setUpdated_at(productVariant.getUpdated_at());
        return response;
    }
}
