package com.lynx.fee_service.controller;

import com.lynx.fee_service.dto.FeeRequest;
import com.lynx.fee_service.dto.FeeResponse;
import com.lynx.fee_service.dto.FeeUpdateRequest;
import com.lynx.fee_service.entity.Fee;
import com.lynx.fee_service.service.FeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

        feeService.createFee(
                request.getOrderId(),
                request.getPlatformUserId(),
                request.getPrice(),
                request.getQuantity(),
                request.getExchangeFee()
        );

        FeeResponse response = new FeeResponse();
        response.setPlatformFee(platformFee);
        response.setTotalCost(total);

        return response;
    }

    @GetMapping("/{id}")
    public Fee getFee(@PathVariable UUID id) {
        return feeService.getFee(id);
    }

    @GetMapping
    public List<Fee> getAllFees() {
        return feeService.getAllFees();
    }

    @PutMapping("/{id}")
    public Fee updateAmount(
            @PathVariable UUID id,
            @RequestParam BigDecimal amount
    ) {
        return feeService.updateAmount(id, amount);
    }

    @PatchMapping("/{id}")
    public Fee updateFee(
            @PathVariable UUID id,
            @RequestBody FeeUpdateRequest request
    ) {
        return feeService.updateFee(id, request);
    }


    @DeleteMapping("/{id}")
    public void deleteFee(@PathVariable UUID id) {
        feeService.deleteFee(id);
    }
}