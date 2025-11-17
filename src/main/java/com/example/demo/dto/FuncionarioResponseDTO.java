package com.example.demo.dto;

public record FuncionarioResponseDTO(
    Long id,
    String nome,
    String email,
    String cargo
) {}