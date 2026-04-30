package com.lynx.fee_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class FeeResponse {
    private UUID orderId;
    private BigDecimal platformFee;
    private BigDecimal totalCost;
}