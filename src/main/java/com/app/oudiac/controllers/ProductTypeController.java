package com.app.oudiac.controllers;

import com.app.oudiac.dtos.productTypeDtos.ProductTypeRequestDto;
import com.app.oudiac.dtos.productTypeDtos.ProductTypeResponseDto;
import com.app.oudiac.services.productTypeService.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-type")
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    @PostMapping("/oudiac/add")
    public ResponseEntity<ProductTypeResponseDto> addProductType(@RequestBody ProductTypeRequestDto request){
        return productTypeService.add(request);
    }

    @GetMapping("/oudiac/get-product-types")
    public ResponseEntity<List<ProductTypeResponseDto>> getProductTypes(){
        return productTypeService.getProductTypes();
    }
    @DeleteMapping("/oudiac/remove/{name}")
    public ResponseEntity<String> remove(@PathVariable String name){
        return productTypeService.remove(name);
    }
}
