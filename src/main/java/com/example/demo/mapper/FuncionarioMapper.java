package com.example.demo.mapper;

import com.example.demo.dto.FuncionarioRequestDTO;
import com.example.demo.dto.FuncionarioResponseDTO;
import com.example.demo.model.Funcionario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component // <-- Informa ao Spring para gerenciar esta classe
public class FuncionarioMapper {

    // Converte RequestDTO (entrada) para Entidade (banco)
    public Funcionario toEntity(FuncionarioRequestDTO dto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());
        funcionario.setCargo(dto.cargo());
        return funcionario;
    }

    // Converte Entidade (banco) para ResponseDTO (saída)
    public FuncionarioResponseDTO toResponseDTO(Funcionario entity) {
        return new FuncionarioResponseDTO(
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.getCargo()
        );
    }

    // Converte uma Lista de Entidades para uma Lista de DTOs
    public List<FuncionarioResponseDTO> toResponseDTOList(List<Funcionario> entityList) {
        return entityList.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }
}