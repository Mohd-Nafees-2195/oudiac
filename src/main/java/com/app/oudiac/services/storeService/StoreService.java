package com.app.oudiac.services.storeService;

import com.app.oudiac.configs.SupabaseConfig.S3Properties;
import com.app.oudiac.dtos.storeDtos.StoreRequestDto;
import com.app.oudiac.dtos.storeDtos.StoreResponseDto;
import com.app.oudiac.exceptions.UserNotFoundException;
import com.app.oudiac.models.Admin;
import com.app.oudiac.models.Store;
import com.app.oudiac.models.User;
import com.app.oudiac.repositories.AdminRepository;
import com.app.oudiac.repositories.StoreRepository;
import com.app.oudiac.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class StoreService implements IStoreService{

    private final StoreRepository storeRepository;
    private final AdminRepository adminRepository;

    private final S3Client s3Client;
    private final S3Properties properties;

    @Value("${S3_STORAGE_BASE_URL}")
    private String s3StorageBaseUrl;

    @Override
    public ResponseEntity<StoreResponseDto> addStore(StoreRequestDto requestDto, MultipartFile[] files) throws IOException {
        Optional<Admin> userOptional=adminRepository.findByEmail(requestDto.getManagerEmail());

        if(userOptional.isEmpty()){
            throw new UserNotFoundException("Manager not exist!! Please enter correct email");
        }

        MultipartFile file=files[0];
        String key = "storeImages/"+ System.currentTimeMillis()
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

        Store newStore=StoreRequestDto.convertStoreRequestDtoToStore(requestDto);
        newStore.setStoreImageUrl(s3StorageBaseUrl+key);
        newStore.setManager(userOptional.get());

        storeRepository.save(newStore);

        StoreResponseDto response=StoreResponseDto.convertStoreToStoreResponseDto(newStore);

        //Later please update below details from  database
        response.setOrdersToday(234L);
        response.setRevenueToday(34592.34);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<StoreResponseDto>> getStores() {
        List<Store> stores=storeRepository.findAll();
        List<StoreResponseDto> response=new ArrayList<>();
        for (Store store:stores){
            response.add(StoreResponseDto.convertStoreToStoreResponseDto(store));
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
