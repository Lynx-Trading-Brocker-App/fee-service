package com.lynx.fee_service.service;

import com.lynx.fee_service.entity.Fee;
import com.lynx.fee_service.exception.FeeNotFoundException;
import com.lynx.fee_service.repository.FeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeServiceTest {

    @Mock
    private FeeRepository feeRepository;

    @InjectMocks
    private FeeService feeService;

    private void setFeeRate(BigDecimal rate) {
        ReflectionTestUtils.setField(feeService, "feeRate", rate);
    }

    @Test
    void shouldCalculatePlatformFee() {
        setFeeRate(new BigDecimal("0.01"));

        BigDecimal result = feeService.calculatePlatformFee(
                new BigDecimal("100"),
                2
        );

        assertEquals(new BigDecimal("2.00"), result);
    }

    @Test
    void shouldCalculateTotalCost() {
        setFeeRate(new BigDecimal("0.01"));

        BigDecimal result = feeService.calculateTotalCost(
                new BigDecimal("100"),
                2,
                new BigDecimal("1")
        );

        assertEquals(new BigDecimal("203.00"), result);
    }

    @Test
    void shouldCreateFee() {
        setFeeRate(new BigDecimal("0.01"));

        when(feeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Fee fee = feeService.createFee(
                "ord-1",
                "user-1",
                new BigDecimal("100"),
                2,
                new BigDecimal("1")
        );

        assertNotNull(fee);
        assertEquals(new BigDecimal("200"), fee.getAmount());
        assertEquals("ord-1", fee.getOrderId());

        verify(feeRepository, times(1)).save(any());
    }

    @Test
    void shouldGetFee() {
        UUID id = UUID.randomUUID();

        Fee fee = Fee.builder().id(id).build();

        when(feeRepository.findById(id)).thenReturn(Optional.of(fee));

        Fee result = feeService.getFee(id);

        assertEquals(id, result.getId());
    }

    @Test
    void shouldThrowWhenFeeNotFound() {
        UUID id = UUID.randomUUID();

        when(feeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FeeNotFoundException.class, () -> feeService.getFee(id));
    }

    @Test
    void shouldGetAllFees() {
        when(feeRepository.findAll()).thenReturn(java.util.List.of());

        assertNotNull(feeService.getAllFees());

        verify(feeRepository, times(1)).findAll();
    }
}