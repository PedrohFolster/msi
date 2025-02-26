package com.example.msi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.msi.entities.TokenRevogado;
import java.time.LocalDateTime;

@Repository
public interface TokenRevogadoRepository extends JpaRepository<TokenRevogado, Long> {
    boolean existsByToken(String token);
    
    @Modifying
    @Query("DELETE FROM TokenRevogado t WHERE t.dataRevogacao <= :data")
    void deleteByDataRevogacaoBefore(LocalDateTime data);
} 