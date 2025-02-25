package com.example.msi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.msi.entities.UserStatus;
import com.example.msi.repository.UserStatusRepository;

import jakarta.annotation.PostConstruct;

@Service
public class InitializationService {

    @Autowired
    private UserStatusRepository userStatusRepository;

    @PostConstruct
    public void init() {
        // Cria os status padrão se não existirem
        if (userStatusRepository.count() == 0) {
            UserStatus ativo = new UserStatus();
            ativo.setStatus("Ativo");
            userStatusRepository.save(ativo);

            UserStatus inativo = new UserStatus();
            inativo.setStatus("Inativo");
            userStatusRepository.save(inativo);
        }
    }
} 