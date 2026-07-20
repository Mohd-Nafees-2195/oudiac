package com.app.oudiac.dtos.productDtos;

import com.app.oudiac.dtos.brandDtos.BrandRequestDto;
import com.app.oudiac.dtos.categoryDtos.CategoryRequestDto;
import com.app.oudiac.dtos.productTypeDtos.ProductTypeRequestDto;
import com.app.oudiac.dtos.productVariantRequestDto.ProductVariantReqDto;
import com.app.oudiac.models.Product;
import com.app.oudiac.models.ProductVariant;
import com.app.oudiac.models.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull
    private ProductStatus productStatus;

    @NotNull
    private Long brandId;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long productTypeId;  //Attar , Bhakhoon

//    @NotBlank(message = "Fragrance family is required")
    private String fragranceFamily;

    @NotNull
    private Long storeId;

  List<ProductVariantReqDto> productVariants;

    public static Product fromProductRequestDtoToProductVariant(ProductRequestDto requestDto) {
        Product newProduct=new Product();
        newProduct.setName(requestDto.getName());
        newProduct.setDescription(requestDto.getDescription());
        newProduct.setFragranceFamily(requestDto.getFragranceFamily());
        newProduct.setProductStatus(requestDto.getProductStatus());

        Date date=new Date();
        newProduct.setCreated_at(date);
        newProduct.setUpdated_at(date);
        newProduct.setIsDeleted(false);

//        newProductVariant.setProduct(newProduct);
//
//        newProductVariant.setSku(requestDto.getSku());
//        newProductVariant.setMRP(requestDto.getMRP());
//        newProductVariant.setSellingPrice(requestDto.getSellingPrice());
//
//        newProductVariant.setVariantType(requestDto.getVariantType());
//        newProductVariant.setStock(requestDto.getQuantity());
//
//        newProductVariant.setCreated_at(date);
//        newProductVariant.setUpdated_at(date);
//        newProductVariant.setIsDeleted(false);

        return newProduct;
    }
}
