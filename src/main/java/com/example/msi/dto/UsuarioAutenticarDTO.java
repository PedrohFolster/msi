package com.example.msi.dto;

import jakarta.validation.constraints.NotBlank;

public class UsuarioAutenticarDTO {

    private Long id;

    @NotBlank
    private String login;
    private String password;

    @NotBlank
    private String perfil;

    public UsuarioAutenticarDTO() {
    }

    public UsuarioAutenticarDTO(Long id, String login, String password, String perfil) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}