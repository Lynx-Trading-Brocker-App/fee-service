package com.lynx.fee_service.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class FeeRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private String platformUserId;

    @NotNull
    @Positive
    private BigDecimal price;

    @Positive
    private int quantity;

    @NotNull
    @PositiveOrZero
    private BigDecimal exchangeFee;
}