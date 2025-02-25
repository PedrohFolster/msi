package com.example.msi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.msi.dto.UsuarioAutenticarDTO;
import com.example.msi.dto.UsuarioDTO;
import com.example.msi.entities.Usuario;
import com.example.msi.security.AuthenticationService;
import com.example.msi.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authService;
    private final UsuarioService usuarioService;

    public AuthenticationController(AuthenticationManager authenticationManager, AuthenticationService authService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioAutenticarDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
        );
        
        String token = authService.authenticate(authentication);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public Usuario register(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        return usuarioService.criar(usuarioDTO);
    }
}