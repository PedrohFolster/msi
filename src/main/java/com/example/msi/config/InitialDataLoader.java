package com.example.msi.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.msi.entities.Usuario;
import com.example.msi.repository.UsuarioRepository;

import java.time.LocalDate;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Verifica se já existe algum admin
        if (!usuarioRepository.existsByRole("ROLE_ADMIN")) {
            Usuario admin = new Usuario();
            admin.setNome("Admin");
            admin.setEmail("admin@admin.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setDataNascimento(LocalDate.now());
            
            usuarioRepository.save(admin);
        }
    }
} 