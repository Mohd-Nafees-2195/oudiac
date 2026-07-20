package com.app.oudiac.dtos.addressDtos;

import com.app.oudiac.models.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AddressRequestDto {

    @NotEmpty(message = "Shipping address cannot be empty")
    private String fullName;

    @NotEmpty(message = "Shipping address cannot be empty")
    private String phoneNumber;

    @NotEmpty(message = "Shipping address cannot be empty")
    private String shippingAddress;

    @NotEmpty(message = "City cannot be empty")
    private String city;

    @NotEmpty(message = "Pin Code cannot be empty")
    private String pinCode;

    @NotEmpty(message = "Country cannot be empty")
    private String country;

    @NotEmpty(message = "State cannot be empty")
    private String state;

    public static Address fromRequestDto(@Valid AddressRequestDto request) {
        Address address=new Address();
        BeanUtils.copyProperties(request,address);
        return address;
    }
}
