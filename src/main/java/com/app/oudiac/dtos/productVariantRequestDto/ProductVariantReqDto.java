package com.app.oudiac.dtos.productVariantRequestDto;

import com.app.oudiac.models.ProductVariant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantReqDto {

    @NotBlank(message = "Variant Type is required")
    private String variantType;  //100ml, 50 ml

    @NotNull
    private BigDecimal sellingPrice;

    @NotNull
    private BigDecimal MRP;

//    @NotBlank(message = "SKU is required")
//    private String sku;

    @NotNull
    private Long stock;

    public static ProductVariant fromProductVariantReqDto(ProductVariantReqDto productVariantReqDto){
        ProductVariant productVariant=new ProductVariant();
        productVariant.setVariantType(productVariantReqDto.getVariantType());
        productVariant.setSellingPrice(productVariantReqDto.getSellingPrice());
        productVariant.setMRP(productVariantReqDto.getMRP());
        productVariant.setStock(productVariantReqDto.getStock());
        return productVariant;
    }
}
