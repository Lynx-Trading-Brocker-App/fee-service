package com.lynx.fee_service.repository;

import com.lynx.fee_service.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeeRepository extends JpaRepository<Fee, UUID> {
}