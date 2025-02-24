package com.example.msi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.msi.entities.Usuario;
import com.example.msi.repository.UsuarioRepository;

import java.time.LocalDate;

@SpringBootApplication
public class MsiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Verifica se já existe algum admin
			if (!usuarioRepository.existsByRole("ROLE_ADMIN")) {
				Usuario admin = new Usuario();
				admin.setNome("Admin");
				admin.setEmail("admin@admin.com");
				admin.setSenha(passwordEncoder.encode("admin123"));
				admin.setRole("ROLE_ADMIN");
				admin.setDataNascimento(LocalDate.of(1990, 1, 1));
				
				usuarioRepository.save(admin);
				System.out.println("Admin padrão criado com sucesso!");
				System.out.println("Email: admin@admin.com");
				System.out.println("Senha: admin123");
			}
		};
	}

}
