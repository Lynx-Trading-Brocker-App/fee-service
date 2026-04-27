package com.lynx.fee_service.service;

import com.lynx.fee_service.dto.FeeUpdateRequest;
import com.lynx.fee_service.entity.Fee;
import com.lynx.fee_service.exception.FeeNotFoundException;
import com.lynx.fee_service.repository.FeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeeService {

    private final FeeRepository feeRepository;

    @Value("${fee.rate}")
    private BigDecimal feeRate;

    public BigDecimal calculatePlatformFee(BigDecimal price, int quantity) {
        return price
                .multiply(BigDecimal.valueOf(quantity))
                .multiply(feeRate);
    }

    public BigDecimal calculateTotalCost(
            BigDecimal price,
            int quantity,
            BigDecimal exchangeFee
    ) {
        BigDecimal base = price.multiply(BigDecimal.valueOf(quantity));
        BigDecimal platformFee = calculatePlatformFee(price, quantity);

        return base.add(exchangeFee).add(platformFee);
    }

    public Fee createFee(
            String orderId,
            String platformUserId,
            BigDecimal price,
            int quantity,
            BigDecimal exchangeFee
    ) {
        BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
        BigDecimal platformFee = amount.multiply(feeRate);

        Fee fee = Fee.builder()
                .orderId(orderId)
                .platformUserId(platformUserId)
                .amount(amount)
                .exchangeFee(exchangeFee)
                .platformFee(platformFee)
                .createdAt(LocalDateTime.now())
                .build();

        return feeRepository.save(fee);
    }

    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    public Fee updateAmount(UUID id, BigDecimal amount) {
        Fee fee = getFee(id);
        fee.setAmount(amount);
        return feeRepository.save(fee);
    }

    public Fee getFee(UUID id) {
        return feeRepository.findById(id)
                .orElseThrow(() -> new FeeNotFoundException("Fee not found"));
    }

    public void deleteFee(UUID id) {
        feeRepository.deleteById(id);
    }

    public Fee updateFee(UUID id, FeeUpdateRequest request) {
        Fee fee = getFee(id);

        if (request.getOrderId() != null) {
            fee.setOrderId(request.getOrderId());
        }

        if (request.getPlatformUserId() != null) {
            fee.setPlatformUserId(request.getPlatformUserId());
        }

        if (request.getAmount() != null) {
            fee.setAmount(request.getAmount());
        }

        if (request.getExchangeFee() != null) {
            fee.setExchangeFee(request.getExchangeFee());
        }

        if (request.getPlatformFee() != null) {
            fee.setPlatformFee(request.getPlatformFee());
        }

        if (request.getCreatedAt() != null) {
            fee.setCreatedAt(request.getCreatedAt());
        }

        return feeRepository.save(fee);
    }
}