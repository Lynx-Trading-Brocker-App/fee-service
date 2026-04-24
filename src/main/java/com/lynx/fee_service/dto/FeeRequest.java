package com.lynx.fee_service.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Getter
@Setter
public class FeeRequest {

    @NotNull
    @Positive
    private BigDecimal price;

    @Positive
    private int quantity;

    @NotNull
    private BigDecimal exchangeFee;
}