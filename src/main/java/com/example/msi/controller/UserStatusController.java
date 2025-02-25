package com.example.msi.controller;

import com.example.msi.entities.UserStatus;
import com.example.msi.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-status")
public class UserStatusController {

    private final UserStatusService userStatusService;

    @Autowired
    public UserStatusController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    @GetMapping
    public List<UserStatus> listarTodos() {
        return userStatusService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStatus> buscarPorId(@PathVariable Long id) {
        UserStatus userStatus = userStatusService.buscarPorId(id);
        return ResponseEntity.ok(userStatus);
    }

    @PostMapping
    public ResponseEntity<UserStatus> criar(@RequestBody UserStatus userStatus) {
        UserStatus novoStatus = userStatusService.criar(userStatus);
        return ResponseEntity.status(201).body(novoStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStatus> atualizar(@PathVariable Long id, @RequestBody UserStatus userStatus) {
        UserStatus statusAtualizado = userStatusService.atualizar(id, userStatus);
        return ResponseEntity.ok(statusAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        userStatusService.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 