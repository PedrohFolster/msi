package com.example.msi.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.msi.dto.UsuarioDTO;
import com.example.msi.entities.Usuario;
import com.example.msi.entities.UsuarioAutenticar;
import com.example.msi.exception.UsuarioException;
import com.example.msi.exception.UsuarioValidationException;
import com.example.msi.repository.UsuarioAutenticarRepository;
import com.example.msi.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioAutenticarRepository autenticarRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, 
                         UsuarioAutenticarRepository autenticarRepository,
                         PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.autenticarRepository = autenticarRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario criar(UsuarioDTO dto) {
        validarCamposObrigatorios(dto);
        
        if (repository.existsByEmail(dto.getEmail())) {
            throw new UsuarioException.EmailJaExisteException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        preencherUsuario(usuario, dto);
        
        // Define ROLE_USER como padrão
        String role = dto.getRole() != null && dto.getRole().equalsIgnoreCase("admin") 
            ? "ROLE_ADMIN" 
            : "ROLE_USER";
        usuario.setRole(role);

        // Codifica a senha usando BCrypt
        String senhaEncoded = passwordEncoder.encode(dto.getSenha());
        usuario.setSenha(senhaEncoded);

        // Salva o usuário principal
        usuario = repository.save(usuario);
        
        // Cria e salva o usuário para autenticação
        UsuarioAutenticar usuarioAuth = new UsuarioAutenticar();
        usuarioAuth.setEmail(usuario.getEmail());
        usuarioAuth.setSenha(senhaEncoded); // Usa a mesma senha codificada
        usuarioAuth.setPerfil(usuario.getRole());
        
        autenticarRepository.save(usuarioAuth);
        
        return usuario;
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {
        validarCamposObrigatorios(dto);
        Usuario usuario = buscarPorId(id);
        
        if (!usuario.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new UsuarioException.EmailJaExisteException("Email já cadastrado");
        }

        preencherUsuario(usuario, dto);
        return repository.save(usuario);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new UsuarioException.UsuarioNaoEncontradoException("Usuário não encontrado");
        }
        repository.deleteById(id);
    }

    private void validarCamposObrigatorios(UsuarioDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new UsuarioValidationException.NomeObrigatorioException();
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new UsuarioValidationException.EmailObrigatorioException();
        }
        if (dto.getDataNascimento() == null) {
            throw new UsuarioValidationException.DataNascimentoObrigatoriaException();
        }
        if (dto.getSenha() == null || dto.getSenha().trim().isEmpty()) {
            throw new UsuarioValidationException.SenhaObrigatoriaException();
        }
    }

    private void preencherUsuario(Usuario usuario, UsuarioDTO dto) {
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setDataNascimento(dto.getDataNascimento());
        // A senha será codificada separadamente
    }

    public boolean existsByRole(String role) {
        return repository.existsByRole(role);
    }
} 