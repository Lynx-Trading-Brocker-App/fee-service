package com.lynx.fee_service.controller;

import com.lynx.fee_service.dto.FeeRequest;
import com.lynx.fee_service.dto.FeeResponse;
import com.lynx.fee_service.service.FeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/fees")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    @PostMapping("/calculate")
    public FeeResponse calculate(@Valid @RequestBody FeeRequest request) {

        BigDecimal platformFee = feeService.calculatePlatformFee(
                request.getPrice(),
                request.getQuantity()
        );

        BigDecimal total = feeService.calculateTotalCost(
                request.getPrice(),
                request.getQuantity(),
                request.getExchangeFee()
        );

        FeeResponse response = new FeeResponse();
        response.setPlatformFee(platformFee);
        response.setTotalCost(total);

        return response;
    }
}