package com.lynx.fee_service.repository;

import com.lynx.fee_service.entity.Fee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class FeeRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private FeeRepository feeRepository;

    private Fee createFee() {
        return Fee.builder()
                .orderId("ord-1")
                .platformUserId("user-1")
                .amount(new BigDecimal("100"))
                .exchangeFee(new BigDecimal("1"))
                .platformFee(new BigDecimal("2"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldSaveFee() {
        Fee saved = feeRepository.save(createFee());

        assertNotNull(saved.getId());
    }

    @Test
    void shouldFindFeeById() {
        Fee saved = feeRepository.save(createFee());

        Fee found = feeRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("ord-1", found.getOrderId());
    }

    @Test
    void shouldFindAllFees() {
        feeRepository.save(createFee());

        List<Fee> fees = feeRepository.findAll();

        assertFalse(fees.isEmpty());
    }

    @Test
    void shouldDeleteFee() {
        Fee saved = feeRepository.save(createFee());
        feeRepository.deleteById(saved.getId());

        assertFalse(feeRepository.findById(saved.getId()).isPresent());
    }
}