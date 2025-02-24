package com.example.msi.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.msi.repository.UsuarioAutenticarRepository;

@Service
public class UserLoginDetailsService implements UserDetailsService {

    private final UsuarioAutenticarRepository usuarioAutenticarRepository;

    public UserLoginDetailsService(UsuarioAutenticarRepository usuarioAutenticarRepository) {
        this.usuarioAutenticarRepository = usuarioAutenticarRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioAutenticarRepository.findByLogin(username)
                .map(UserAuthenticated::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário não encontrado: " + username)
                );
    }
}
