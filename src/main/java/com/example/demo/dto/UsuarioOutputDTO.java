package com.example.demo.dto;

public class UsuarioOutputDTO {

    private Long id;
    private String nome;
    private String email;
    private String cargo;

    public UsuarioOutputDTO(Long id, String nome, String email, String cargo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCargo() {
        return cargo;
    }
}
