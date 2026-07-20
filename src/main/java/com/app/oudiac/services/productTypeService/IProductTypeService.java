package com.app.oudiac.services.productTypeService;

import com.app.oudiac.dtos.productTypeDtos.ProductTypeRequestDto;
import com.app.oudiac.dtos.productTypeDtos.ProductTypeResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductTypeService {
    ResponseEntity<ProductTypeResponseDto> add(ProductTypeRequestDto request);

    ResponseEntity<List<ProductTypeResponseDto>> getProductTypes();

    ResponseEntity<String> remove(String name);
}
