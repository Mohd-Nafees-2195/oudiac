package com.app.oudiac.dtos.productDtos;

import com.app.oudiac.dtos.brandDtos.BrandResponseDto;
import com.app.oudiac.dtos.categoryDtos.CategoryResponseDto;
import com.app.oudiac.dtos.productTypeDtos.ProductTypeResponseDto;
import com.app.oudiac.dtos.productVariantRequestDto.ProductVariantResDto;
import com.app.oudiac.models.*;
import com.app.oudiac.models.enums.ProductStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private ProductStatus productStatus;
    private String imageUrl;

    private BrandResponseDto brand;
    private CategoryResponseDto category;
    private ProductTypeResponseDto productType;
    private String fragranceFamily;
    private List<Review> reviews;

    private Date createdAt;
    private Date updatedAt;
    private String code;
    List<ProductVariantResDto>  productVariants;


    public static ProductResponseDto fromProductToProductResponseDto(Product product) {
        ProductResponseDto response=new ProductResponseDto();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setBrand(BrandResponseDto.fromBrandToBrandResponseDto(product.getBrand()));
        response.setCategory(CategoryResponseDto.fromCategoryToCategoryResponseDto(product.getCategory()));
        response.setProductType(ProductTypeResponseDto.fromProductType(product.getProductType()));
        response.setFragranceFamily(product.getFragranceFamily());
        response.setCode(product.getCode());
        response.setProductStatus(product.getProductStatus());

        List<ProductVariantResDto> productVariantResDtos=new ArrayList<>();
       for(ProductVariant productVariant:product.getProductVariants()){
           productVariantResDtos.add(ProductVariantResDto.fromProductVariant(productVariant));
       }
       response.setProductVariants(productVariantResDtos);
       response.setImageUrl(product.getImageUrl());
       response.setCreatedAt(product.getCreated_at());
       response.setUpdatedAt(product.getUpdated_at());

        //Set review if required
        return response;
    }
}
