package com.app.oudiac.services.categoryService;

import com.app.oudiac.dtos.categoryDtos.CategoryRequestDto;
import com.app.oudiac.dtos.categoryDtos.CategoryResponseDto;
import com.app.oudiac.exceptions.ItemAlreadyExitException;
import com.app.oudiac.exceptions.ItemNotFoundException;
import com.app.oudiac.models.Category;
import com.app.oudiac.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.utils.StringUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public ResponseEntity<CategoryResponseDto> add(CategoryRequestDto request) {
        Optional<Category> category=categoryRepository.findByCode(request.getCode().toUpperCase());

        if(category.isPresent()){
            throw new ItemAlreadyExitException("Category already exit");
        }
        Category newCategory=CategoryRequestDto.fromCategoryRequestDtoToCategory(request);
        categoryRepository.save(newCategory);

        CategoryResponseDto response=CategoryResponseDto.fromCategoryToCategoryResponseDto(newCategory);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<Category> categories=categoryRepository.findAll();
        List<CategoryResponseDto> response=new ArrayList<>();
        for (Category category:categories){
            response.add(CategoryResponseDto.fromCategoryToCategoryResponseDto(category));
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> remove(String code) throws RuntimeException {
        if(StringUtils.isBlank(code)){
            throw new InvalidParameterException("Invalid Parameter");
        }
        Optional<Category> category=categoryRepository.findByCode(code.toUpperCase());
        if(category.isEmpty()){
            throw new ItemNotFoundException("Item not found");
        }
       categoryRepository.deleteByCode(code.toUpperCase());
       return new ResponseEntity<>("Success",HttpStatus.OK);
    }

    public ResponseEntity<List<CategoryResponseDto>> getCategoryTabs() {
        List<Category> categories=categoryRepository.findAll();
        List<CategoryResponseDto> response=new ArrayList<>();
        for (Category category:categories){
            if(!"ALL".equalsIgnoreCase(category.getCode())){
                response.add(CategoryResponseDto.fromCategoryToCategoryResponseDto(category));
            }
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
