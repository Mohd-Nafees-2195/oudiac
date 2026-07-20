package com.app.oudiac.dtos.addressDtos;


import com.app.oudiac.models.Address;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AddressResponseDto {
    private Long id;
    private String fullName;
    private String shippingAddress;
    private String city;
    private String country;
    private String pinCode;
    private String phoneNumber;

    public static AddressResponseDto fromAddressEntity(Address newAddress) {
        AddressResponseDto response=new AddressResponseDto();
        BeanUtils.copyProperties(newAddress,response);
        return response;
    }
}
