package com.lynx.fee_service.repository;

import com.lynx.fee_service.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface FeeRepository extends JpaRepository<Fee, UUID> {
    @Query("SELECT SUM(f.platformFee) FROM Fee f")
    BigDecimal sumTotalPlatformFee();
}