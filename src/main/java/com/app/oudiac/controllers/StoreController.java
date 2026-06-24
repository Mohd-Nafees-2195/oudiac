package com.app.oudiac.controllers;

import com.app.oudiac.dtos.productDtos.ProductRequestDto;
import com.app.oudiac.dtos.productDtos.ProductResponseDto;
import com.app.oudiac.dtos.storeDtos.StoreRequestDto;
import com.app.oudiac.dtos.storeDtos.StoreResponseDto;
import com.app.oudiac.services.storeService.StoreService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping(value = "/oudiac/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoreResponseDto> addStore(@Valid @ModelAttribute StoreRequestDto requestDto, @RequestParam("images") MultipartFile[] images){
        try{
            return storeService.addStore(requestDto,images);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // /api/stores/admin/oudiac/get-stores
    // /api/admin/oudiac/get-stores
    @GetMapping("/admin/oudiac/get-stores")
    public ResponseEntity<List<StoreResponseDto>> getStores(){
        return storeService.getStores();
    }


}
