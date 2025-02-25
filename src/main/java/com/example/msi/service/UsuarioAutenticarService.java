package com.example.msi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.msi.entities.UsuarioAutenticar;
import com.example.msi.repository.UsuarioAutenticarRepository;
import com.example.msi.util.Hashing;

@Service
public class UsuarioAutenticarService {

    @Autowired
    private UsuarioAutenticarRepository usuarioAutenticarRepository;

    public boolean authenticate(String email, String senha) {
        UsuarioAutenticar usuarioAutenticar = usuarioAutenticarRepository.findByEmail(email).orElse(null);
        if (usuarioAutenticar != null) {
            return Hashing.matches(senha, usuarioAutenticar.getSenha());
        }
        return false;
    }

    public UsuarioAutenticar findByEmail(String email) {
        return usuarioAutenticarRepository.findByEmail(email).orElse(null);
    }
}