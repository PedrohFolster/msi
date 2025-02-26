package com.example.msi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.msi.entities.TokenRevogado;

@Repository
public interface TokenRevogadoRepository extends JpaRepository<TokenRevogado, Long> {
    boolean existsByToken(String token);
} 