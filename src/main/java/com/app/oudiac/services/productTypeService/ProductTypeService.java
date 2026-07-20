package com.app.oudiac.services.productTypeService;

import com.app.oudiac.dtos.productTypeDtos.ProductTypeRequestDto;
import com.app.oudiac.dtos.productTypeDtos.ProductTypeResponseDto;
import com.app.oudiac.exceptions.ItemAlreadyExitException;
import com.app.oudiac.exceptions.ItemNotFoundException;
import com.app.oudiac.models.Category;
import com.app.oudiac.models.ProductType;
import com.app.oudiac.repositories.ProductTypeRepository;
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
public class ProductTypeService implements IProductTypeService{

    protected final ProductTypeRepository productTypeRepository;

    @Transactional
    @Override
    public ResponseEntity<ProductTypeResponseDto> add(ProductTypeRequestDto request) {
        Optional<ProductType> optionalP=productTypeRepository.findByCode(request.getCode().toUpperCase());
        if(optionalP.isPresent()){
            throw new ItemAlreadyExitException("Item Already exist");
        }
        ProductType productType=ProductTypeRequestDto.toProductType(request);
        productTypeRepository.save(productType);

        ProductTypeResponseDto response=ProductTypeResponseDto.fromProductType(productType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ProductTypeResponseDto>> getProductTypes() {
        List<ProductType> productTypes=productTypeRepository.findAll();
        List<ProductTypeResponseDto> response=new ArrayList<>();

        for(ProductType productType:productTypes){
            response.add(ProductTypeResponseDto.fromProductType(productType));
        }

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<String> remove(String code) {
        if(StringUtils.isBlank(code)){
            throw new InvalidParameterException("Invalid Parameter");
        }
        Optional<ProductType> productType=productTypeRepository.findByCode(code.toUpperCase());
        if(productType.isEmpty()){
            throw new ItemNotFoundException("Item not found");
        }
        productTypeRepository.deleteByCode(code.toUpperCase());
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }
}
