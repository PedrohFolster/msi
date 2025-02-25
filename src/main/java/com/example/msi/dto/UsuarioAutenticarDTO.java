package com.example.msi.dto;

import jakarta.validation.constraints.NotBlank;

public class UsuarioAutenticarDTO {

    private Long id;

    @NotBlank
    private String email;
    private String senha;

    @NotBlank
    private String perfil;

    public UsuarioAutenticarDTO() {
    }

    public UsuarioAutenticarDTO(Long id, String email, String senha, String perfil) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}