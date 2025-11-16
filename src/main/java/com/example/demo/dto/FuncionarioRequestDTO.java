package com.example.demo.dto;

public record FuncionarioRequestDTO(
    String nome,
    String email,
    String cargo
) {}