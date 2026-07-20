package com.app.oudiac.controllers;

import com.app.oudiac.dtos.brandDtos.BrandRequestDto;
import com.app.oudiac.dtos.brandDtos.BrandResponseDto;
import com.app.oudiac.services.brandService.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brand")
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/oudiac/add")
    public ResponseEntity<BrandResponseDto> add(@Valid @RequestBody BrandRequestDto request){
        return brandService.add(request);
    }
    @GetMapping("/oudiac/get-brands")
    public ResponseEntity<List<BrandResponseDto>> getCategories(){
        return brandService.getBrands();
    }

    @DeleteMapping("/oudiac/remove/{name}")
    public ResponseEntity<String> remove(@PathVariable String name){
        return brandService.remove(name);
    }
}
