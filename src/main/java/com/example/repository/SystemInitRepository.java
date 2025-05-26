package com.example.repository;

import com.example.entity.SystemInit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemInitRepository extends JpaRepository<SystemInit, Long> {
    boolean existsByInitializedTrue();
} 