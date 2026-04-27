package com.lynx.fee_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FeeResponse {
    private BigDecimal platformFee;
    private BigDecimal totalCost;
}