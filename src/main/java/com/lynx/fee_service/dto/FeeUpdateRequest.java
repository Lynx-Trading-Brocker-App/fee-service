package com.lynx.fee_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class FeeUpdateRequest {

    private UUID orderId;
    private String platformUserId;
    private BigDecimal amount;
    private BigDecimal exchangeFee;
    private BigDecimal platformFee;
    private LocalDateTime createdAt;
}
