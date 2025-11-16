package com.example.demo.repository;

import com.example.demo.model.Funcionario; // Verifique se este import está correto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importe este

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    // ADICIONE/GARANTA ESTA LINHA:
    Optional<Funcionario> findByEmail(String email);

}