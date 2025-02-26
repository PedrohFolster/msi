package com.example.msi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.msi.repository.TokenRevogadoRepository;
import java.time.LocalDateTime;

@Service
public class TokenLimpezaService {

    private final TokenRevogadoRepository tokenRevogadoRepository;

    @Autowired
    public TokenLimpezaService(TokenRevogadoRepository tokenRevogadoRepository) {
        this.tokenRevogadoRepository = tokenRevogadoRepository;
    }

    @Scheduled(fixedRate = 36000000) // Executa a cada 10 horas (36000000 ms)
    @Transactional
    public void limparTokensExpirados() {
        LocalDateTime limiteExpiracao = LocalDateTime.now().minusHours(10);
        System.out.println("Iniciando limpeza de tokens expirados antes de: " + limiteExpiracao);
        tokenRevogadoRepository.deleteByDataRevogacaoBefore(limiteExpiracao);
        System.out.println("Limpeza de tokens concluída");
    }
} 