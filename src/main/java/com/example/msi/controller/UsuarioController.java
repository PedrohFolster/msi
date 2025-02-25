package com.example.msi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.msi.dto.UsuarioDTO;
import com.example.msi.entities.Usuario;
import com.example.msi.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioDTO dto) {
        return service.atualizar(id, dto);
    }

    // Endpoint para inativar um usuário
    @PostMapping("/inativar/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas administradores podem acessar este endpoint
    public ResponseEntity<String> inativarUsuario(@PathVariable Long id) {
        service.inativar(id); // Chama o método de inativação do serviço
        return ResponseEntity.ok("Usuário inativado com sucesso.");
    }

    // Endpoint para excluir um usuário do banco
    @DeleteMapping("/excluir/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas administradores podem acessar este endpoint
    public ResponseEntity<String> excluirUsuario(@PathVariable Long id) {
        service.deletar(id); // Chama o método de deleção do serviço
        return ResponseEntity.ok("Usuário excluído com sucesso.");
    }
} 