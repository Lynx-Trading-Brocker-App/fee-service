package com.lynx.fee_service.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Service
public class FeeService {

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
}