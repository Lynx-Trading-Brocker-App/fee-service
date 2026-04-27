package com.lynx.fee_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class FeeUpdateRequest {

    private String orderId;
    private String platformUserId;
    private BigDecimal amount;
    private BigDecimal exchangeFee;
    private BigDecimal platformFee;
    private LocalDateTime createdAt;
}
