package com.lynx.fee_service.controller;

import com.lynx.fee_service.dto.FeeRequest;
import com.lynx.fee_service.dto.FeeResponse;
import com.lynx.fee_service.exception.ForbiddenException;
import com.lynx.fee_service.service.FeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;


@RestController
@RequestMapping("internal/fees")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    @Value("${internal.api-key}")
    private String internalApiKey;

    private void validateKey(String key) {
        if (!internalApiKey.equals(key)) {
            throw new ForbiddenException();
        }
    }

    @PostMapping("/calculate")
    public FeeResponse calculate(
            @RequestHeader("X-INTERNAL-KEY") String key,
            @Valid @RequestBody FeeRequest request) {

       validateKey(key);

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
        response.setOrderId(request.getOrderId());
        response.setPlatformFee(platformFee);
        response.setTotalCost(total);

        return response;
    }

    @PostMapping("/record")
    public void record(
            @RequestHeader("X-INTERNAL-KEY") String key,
            @Valid @RequestBody FeeRequest request) {

        validateKey(key);

        feeService.saveFee(
                request.getOrderId(),
                request.getPlatformUserId(),
                request.getPrice(),
                request.getQuantity(),
                request.getExchangeFee()
        );
    }

    @GetMapping("/revenue")
    public Map<String, BigDecimal> getRevenue(@RequestHeader("X-INTERNAL-KEY") String key) {
        validateKey(key);
        return Map.of("total_revenue", feeService.getTotalRevenue());
    }

    @GetMapping("/rate")
    public BigDecimal getRate(@RequestHeader("X-INTERNAL-KEY") String key) {
        validateKey(key);
        return feeService.getFeeRate();
    }

    @PatchMapping("/rate")
    public void updateRate(
            @RequestHeader("X-INTERNAL-KEY") String key,
            @RequestBody Map<String, BigDecimal> body) {
        validateKey(key);
        BigDecimal newRate = body.get("rate");
        if (newRate != null) {
            feeService.updateFeeRate(newRate);
        }
    }

}