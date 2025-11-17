package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}

