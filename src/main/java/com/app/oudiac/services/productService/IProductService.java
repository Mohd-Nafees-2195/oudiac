package com.app.oudiac.services.productService;

import com.app.oudiac.dtos.productDtos.ProductRequestDto;
import com.app.oudiac.dtos.productDtos.ProductResponseDto;
import com.app.oudiac.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IProductService {
    ResponseEntity<ProductResponseDto> addProduct(ProductRequestDto requestDto, MultipartFile[] files) throws IOException;

    Page<ProductResponseDto> getProducts(Integer page, Integer size);

    ResponseEntity<ProductResponseDto> findById(Long id);

    ResponseEntity<ProductResponseDto> findByCategoryId(Long id);
}
