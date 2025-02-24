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

    public boolean authenticate(String username, String password) {
        UsuarioAutenticar usuarioAutenticar = usuarioAutenticarRepository.findByLogin(username).orElse(null);
        if (usuarioAutenticar != null) {
            return Hashing.matches(password, usuarioAutenticar.getPassword());
        }
        return false;
    }

    public UsuarioAutenticar findByLogin(String login) {
        return usuarioAutenticarRepository.findByLogin(login).orElse(null);
    }
}