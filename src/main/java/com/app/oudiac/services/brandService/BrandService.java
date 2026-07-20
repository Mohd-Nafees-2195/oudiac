package com.app.oudiac.services.brandService;

import com.app.oudiac.dtos.brandDtos.BrandRequestDto;
import com.app.oudiac.dtos.brandDtos.BrandResponseDto;
import com.app.oudiac.exceptions.ItemAlreadyExitException;
import com.app.oudiac.exceptions.ItemNotFoundException;
import com.app.oudiac.models.Brand;
import com.app.oudiac.models.Category;
import com.app.oudiac.repositories.BrandRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional
    public ResponseEntity<BrandResponseDto> add(@Valid BrandRequestDto request) {
        Optional<Brand> brand=brandRepository.findByCode(request.getCode().toUpperCase());
        if(brand.isPresent()){
            throw new ItemAlreadyExitException("Brand already present");
        }

        Brand newBrand=BrandRequestDto.fromBrandRequestDtoBrand(request);
        brandRepository.save(newBrand);

        BrandResponseDto response=BrandResponseDto.fromBrandToBrandResponseDto(newBrand);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<BrandResponseDto>> getBrands() {
        List<Brand> brands=brandRepository.findAll();
        List<BrandResponseDto> response=new ArrayList<>();

        for (Brand brand:brands){
            response.add(BrandResponseDto.fromBrandToBrandResponseDto(brand));
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> remove(String name) {
        if(StringUtils.isBlank(name)){
            throw new InvalidParameterException("Invalid Parameter");
        }
        Optional<Brand> brand=brandRepository.findByCode(name.toUpperCase());
        if(brand.isEmpty()){
            throw new ItemNotFoundException("Item not found");
        }
        brandRepository.deleteByCode(name.toUpperCase());
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }
}
