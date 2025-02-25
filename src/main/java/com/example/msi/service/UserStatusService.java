package com.example.msi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.msi.entities.UserStatus;
import com.example.msi.repository.UserStatusRepository;

@Service
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Autowired
    public UserStatusService(UserStatusRepository userStatusRepository) {
        this.userStatusRepository = userStatusRepository;
    }

    public List<UserStatus> listarTodos() {
        return userStatusRepository.findAll();
    }

    public UserStatus buscarPorId(Long id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status não encontrado"));
    }

    public UserStatus criar(UserStatus userStatus) {
        return userStatusRepository.save(userStatus);
    }

    public UserStatus atualizar(Long id, UserStatus userStatus) {
        UserStatus statusExistente = buscarPorId(id);
        statusExistente.setStatus(userStatus.getStatus());
        return userStatusRepository.save(statusExistente);
    }

    public void deletar(Long id) {
        UserStatus statusExistente = buscarPorId(id);
        userStatusRepository.delete(statusExistente);
    }
} 