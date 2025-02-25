package com.example.msi.service;

import com.example.msi.entities.Usuario;
import com.example.msi.exception.UsuarioException;
import com.example.msi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public CustomAuthenticationService(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    public void login(String email, String senha) throws UsuarioException.UsuarioNaoEncontradoException, UsuarioException.UsuarioInativoException, UsuarioException.AutenticacaoFalhouException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException("Usuário não encontrado"));

        // Verifica se o usuário está inativo
        verificarStatus(usuario);

        // Tenta autenticar o usuário
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, senha));
            // Se a autenticação for bem-sucedida, você pode continuar com o processo
        } catch (AuthenticationException e) {
            throw new UsuarioException.AutenticacaoFalhouException("Falha na autenticação");
        }
    }

    private void verificarStatus(Usuario usuario) throws UsuarioException.UsuarioInativoException {
        // Verifica se o usuário está inativo
        if (usuario.getStatus().getId() == 2) {
            throw new UsuarioException.UsuarioInativoException("Usuário inativo não pode realizar esta ação");
        }
    }
} 