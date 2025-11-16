package com.example.demo.controller;

import com.example.demo.entity.Funcionario;
import com.example.demo.repository.FuncionarioRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import com.example.demo.exception.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioRepository repository;

    public FuncionarioController(FuncionarioRepository repository) {
        this.repository = repository;
    }

    // CREATE
    @PostMapping
    public Funcionario criar(@RequestBody Funcionario funcionario) {
        return repository.save(funcionario);
    }

    // READ - listar todos
    @GetMapping
    public List<Funcionario> listar() {
        return repository.findAll();
    }

    // READ - buscar por ID
    @GetMapping("/{id}")
    public Funcionario buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    // UPDATE
    @PutMapping("/{id}")
    public Funcionario atualizar(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        Funcionario existente = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        existente.setNome(funcionario.getNome());
        existente.setCargo(funcionario.getCargo());

        return repository.save(existente);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}


