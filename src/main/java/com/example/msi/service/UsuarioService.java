package com.example.msi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.msi.dto.UsuarioDTO;
import com.example.msi.dto.UsuarioResponseDTO;
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

    @Autowired
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
        
        String role = dto.getRole() != null && dto.getRole().equalsIgnoreCase("admin") 
            ? "ROLE_ADMIN" 
            : dto.getRole() != null && dto.getRole().equalsIgnoreCase("deleted") 
            ? "ROLE_DELETED" 
            : "ROLE_USER";
        usuario.setRole(role);
    
        // Codifica a senha usando BCrypt
        String senhaEncoded = passwordEncoder.encode(dto.getSenha());
        usuario.setSenha(senhaEncoded);
    
        usuario = repository.save(usuario);
        
        // Cria e salva o usuário para autenticação
        UsuarioAutenticar usuarioAuth = new UsuarioAutenticar();
        usuarioAuth.setEmail(usuario.getEmail());
        usuarioAuth.setSenha(senhaEncoded);
        usuarioAuth.setRole(usuario.getRole());
        
        autenticarRepository.save(usuarioAuth);
        
        return usuario;
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    public Page<Usuario> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<UsuarioResponseDTO> listarTodosResponse(Pageable pageable) {
        Page<Usuario> usuarios = repository.findAll(pageable);
        return usuarios.getContent().stream()
                .map(usuario -> new UsuarioResponseDTO(
                    usuario.getId(), 
                    usuario.getNome(), 
                    usuario.getEmail(), 
                    usuario.getDataNascimento() != null ? usuario.getDataNascimento().toString() : null,
                    usuario.getRole()))
                .collect(Collectors.toList());
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {
        validarCamposObrigatorios(dto);
        Usuario usuario = buscarPorId(id);
        
        // Busca o UsuarioAutenticar antes de alterar o email
        UsuarioAutenticar usuarioAutenticar = autenticarRepository.findByEmail(usuario.getEmail())
                .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException("Usuário de autenticação não encontrado"));
        
        if (!usuario.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new UsuarioException.EmailJaExisteException("Email já cadastrado");
        }

        preencherUsuario(usuario, dto);
        usuario = repository.save(usuario);

        // Atualiza o UsuarioAutenticar
        usuarioAutenticar.setEmail(usuario.getEmail());
        usuarioAutenticar.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuarioAutenticar.setRole(usuario.getRole());
        autenticarRepository.save(usuarioAutenticar);

        return usuario;
    }

    @Transactional
    public void inativar(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException("Usuário não encontrado"));
        
        // Atribui a role "ROLE_DELETED" ao usuário
        usuario.setRole("ROLE_DELETED");

        UsuarioAutenticar usuarioAutenticar = autenticarRepository.findByEmail(usuario.getEmail())
                .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException("Usuário de autenticação não encontrado"));
        
        usuarioAutenticar.setRole("ROLE_DELETED");
        autenticarRepository.save(usuarioAutenticar);
    
        repository.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException("Usuário não encontrado"));

        // Verifica se o usuário de autenticação existe antes de tentar excluí-lo
        UsuarioAutenticar usuarioAutenticar = autenticarRepository.findByEmail(usuario.getEmail())
                .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException("Usuário de autenticação não encontrado"));

        autenticarRepository.delete(usuarioAutenticar);
        repository.delete(usuario);
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
    }

    public boolean existsByRole(String role) {
        return repository.existsByRole(role);
    }
} 