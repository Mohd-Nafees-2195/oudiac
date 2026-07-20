package com.app.oudiac.services.productService;

import com.app.oudiac.configs.SupabaseConfig.S3Properties;
import com.app.oudiac.dtos.productDtos.ProductRequestDto;
import com.app.oudiac.dtos.productDtos.ProductResponseDto;
import com.app.oudiac.dtos.productVariantRequestDto.ProductVariantReqDto;
import com.app.oudiac.exceptions.ItemAlreadyExitException;
import com.app.oudiac.exceptions.ItemNotFoundException;
import com.app.oudiac.models.*;
import com.app.oudiac.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final ProductVariantRepository productVariantRepository;
    private final ProductTypeRepository productTypeRepository;
    private final StoreRepository storeRepository;

    private final S3Client s3Client;
    private final S3Properties properties;

    @Value("${S3_STORAGE_BASE_URL}")
    private String s3StorageBaseUrl;

    @Transactional
    @Override
    public ResponseEntity<ProductResponseDto> addProduct(ProductRequestDto requestDto, MultipartFile[] files) throws IOException {

        //Check Store availability
        Optional<Store> store=storeRepository.findById(requestDto.getStoreId());
        if(store.isEmpty()){
            throw new ItemNotFoundException("Please Select the store");
        }

        MultipartFile file=files[0];
        String key = "productImages/"+ System.currentTimeMillis()
                + "-"
                + file.getOriginalFilename();

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(properties.getBucket())
                        .key(key)
                        .contentType(file.getContentType())
                        .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(file.getBytes())
        );

        Product newProduct=ProductRequestDto.fromProductRequestDtoToProductVariant(requestDto);

        List<Store> stores=new ArrayList<>();
        stores.add(store.get());
        newProduct.setStores(stores);
        //Check brand availability in db if not add new brand
        Optional<Brand> brandOptional=brandRepository.findById(requestDto.getBrandId());
        if(brandOptional.isEmpty()){
           throw new ItemNotFoundException("Please Select the brand");
        }
        newProduct.setBrand(brandOptional.get());

        //Check Product type availability in db if not add new brand
        Optional<ProductType> productTypeOptional=productTypeRepository.findById(requestDto.getProductTypeId());
        if(productTypeOptional.isEmpty()){
            throw new ItemNotFoundException("Please Select the Product Type");
        }
        newProduct.setProductType(productTypeOptional.get());

        //Check category availability in db if not add new category
        Optional<Category> categoryOptional=categoryRepository.findById(requestDto.getCategoryId());
        if(categoryOptional.isEmpty()){
            throw new ItemNotFoundException("Please Select the category");
        }
        newProduct.setCategory(categoryOptional.get());

        newProduct.setImageUrl(s3StorageBaseUrl+key);
        String code=store.get().getStoreSku()+"-"+categoryOptional.get().getCode()+"-"+brandOptional.get().getCode()+"-"+productTypeOptional.get().getCode();
        Optional<Product> productOptional=productRepository.findByCode(code);
        if(productOptional.isPresent()){
            throw new ItemAlreadyExitException("Product already exists!! Please got to product and update variants");
        }
        newProduct.setCode(code);
        List<ProductVariant> productVariants=new ArrayList<>();
        for(ProductVariantReqDto productVariantReqDto:requestDto.getProductVariants()){
            ProductVariant productVariant=ProductVariantReqDto.fromProductVariantReqDto(productVariantReqDto);
            productVariant.setSku(code+"-"+productVariantReqDto.getVariantType());
            productVariant.setProduct(newProduct);
            productVariants.add(productVariant);
        }
        newProduct.setProductVariants(productVariants);
//        String code=getProductCode(requestDto.getSku());
//        Optional<Product> productOptional=productRepository.findByCode(code);
//        if(productOptional.isEmpty()){
//            newProduct.setCode(code);
//            productRepository.save(newProduct);
//        }else {
//            newProductVariant.setProduct(productOptional.get());
//        }

        productRepository.save(newProduct);

        ProductResponseDto responseDto=ProductResponseDto.fromProductToProductResponseDto(newProduct);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Override
    public Page<ProductResponseDto> getProducts(Integer page, Integer size) {
        Page<Product> productPage = productRepository.findAll(PageRequest.of(page, size));
        return productPage.map(ProductResponseDto::fromProductToProductResponseDto);
    }

    @Override
    public ResponseEntity<ProductResponseDto> findById(Long id) {
        Optional<Product> productVariant=productRepository.findById(id);
        if(productVariant.isEmpty()){
            throw new ItemNotFoundException("Product Variant Not Found");
        }
        ProductResponseDto response=ProductResponseDto.fromProductToProductResponseDto(productVariant.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductResponseDto> findByCategoryId(Long id) {
//        List<ProductVariant> productVariants=productVariantRepository.findByCategoryId(id);
        return null;
    }

    public String getProductCode(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }

        int lastHyphenIndex = sku.lastIndexOf('-');

        if (lastHyphenIndex <= 0) {
            throw new IllegalArgumentException("Invalid SKU format: " + sku);
        }

        return sku.substring(0, lastHyphenIndex);
    }

}
