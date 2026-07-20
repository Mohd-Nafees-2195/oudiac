package com.app.oudiac.controllers;

import com.app.oudiac.dtos.categoryDtos.CategoryRequestDto;
import com.app.oudiac.dtos.categoryDtos.CategoryResponseDto;
import com.app.oudiac.dtos.storeDtos.StoreResponseDto;
import com.app.oudiac.services.categoryService.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/oudiac/add")
    public ResponseEntity<CategoryResponseDto> add(@Valid @RequestBody CategoryRequestDto request){
        return categoryService.add(request);
    }

    @GetMapping("/oudiac/get-categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategories(){
        return categoryService.getCategories();
    }

    @DeleteMapping("/oudiac/remove/{code}")
    public ResponseEntity<String> remove(@PathVariable String code){
        return categoryService.remove(code);
    }

    @GetMapping("/public/get-categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategoryTabs(){
        return categoryService.getCategoryTabs();
    }
}
