package com.app.oudiac.services.orderChrgesService;

import com.app.oudiac.dtos.OrderChargesDtos.OrderChargesRequestDto;
import com.app.oudiac.dtos.OrderChargesDtos.OrderChargesResponseDto;
import com.app.oudiac.exceptions.ItemNotFoundException;
import com.app.oudiac.models.OrderCharges;
import com.app.oudiac.repositories.OrderChargesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderChargesService {

    private final OrderChargesRepository orderChargesRepository;

    public ResponseEntity<OrderChargesResponseDto> addOrderCharges(OrderChargesRequestDto request) {
        validateFields(request);
        OrderCharges orderCharges=orderChargesRepository.getCharges(false);
        if(orderCharges!=null){
            orderCharges.setIsDeleted(true);
            orderCharges.setUpdated_at(new Date());
            orderChargesRepository.save(orderCharges);
        }
        OrderCharges newOrderCharges=OrderChargesRequestDto.fromOrderChargesRequestDto(request);
        orderChargesRepository.save(newOrderCharges);
        OrderChargesResponseDto response=OrderChargesResponseDto.fromOrderCharges(newOrderCharges);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public void validateFields(OrderChargesRequestDto request){
        if(request.getGst().intValue()<0 || request.getShippingFee().intValue()<=0 || request.getFreeShippingThreshold().intValue()<=0){
            throw new InvalidParameterException("Invalid Values");
        }
    }
}
