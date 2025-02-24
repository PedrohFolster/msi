package com.example.msi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.msi.entities.UsuarioAutenticar;

@Repository
public interface UsuarioAutenticarRepository extends JpaRepository<UsuarioAutenticar, Long> {
    Optional<UsuarioAutenticar> findByLogin(String login);
}
