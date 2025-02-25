package com.example.msi.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.msi.entities.UserStatus;
import com.example.msi.entities.Usuario;
import com.example.msi.repository.UserStatusRepository;
import com.example.msi.repository.UsuarioRepository;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(UsuarioRepository usuarioRepository, 
                             UserStatusRepository userStatusRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.userStatusRepository = userStatusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Verifica se já existe algum admin
        if (!usuarioRepository.existsByRole("ROLE_ADMIN")) {
            Usuario admin = new Usuario();
            admin.setNome("Admin");
            admin.setEmail("admin@admin.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setDataNascimento(LocalDate.now());

            // Define o status como ativo (ID 1)
            UserStatus statusAtivo = userStatusRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Status ativo não encontrado"));
            admin.setStatus(statusAtivo); // Atribui o status ativo ao usuário admin
            
            usuarioRepository.save(admin);
        }
    }
} 