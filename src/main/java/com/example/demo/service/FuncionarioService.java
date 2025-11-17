package com.example.demo.service;

import com.example.demo.dto.FuncionarioRequestDTO;
import com.example.demo.dto.FuncionarioResponseDTO;
import com.example.demo.exception.FuncionarioNotFoundException;
import com.example.demo.mapper.FuncionarioMapper;
import com.example.demo.model.Funcionario;
import com.example.demo.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired // Injeta o nosso conversor
    private FuncionarioMapper mapper;

    // Método de validação (permanece o mesmo)
    private void validarFuncionario(Funcionario funcionario, boolean isUpdate) {
        if (!StringUtils.hasText(funcionario.getNome())) {
            throw new IllegalArgumentException("O nome do funcionário é obrigatório.");
        }
        Optional<Funcionario> funcExistente = funcionarioRepository.findByEmail(funcionario.getEmail());
        if (funcExistente.isPresent()) {
            if (isUpdate && funcExistente.get().getId().equals(funcionario.getId())) {
                // OK
            } else {
                throw new IllegalArgumentException("O e-mail informado já está em uso.");
            }
        }
    }
    
    // --- MÉTODOS CRUD ATUALIZADOS PARA USAR DTOs ---

    // Método interno para buscar a ENTIDADE (usado por 'update' e 'delete')
    private Funcionario getFuncionarioEntityById(Long id) {
        return funcionarioRepository.findById(id)
            .orElseThrow(() -> new FuncionarioNotFoundException("Funcionário não encontrado com o ID: " + id));
    }

    // CREATE: Recebe DTO, retorna DTO
    public FuncionarioResponseDTO criarFuncionario(FuncionarioRequestDTO dto) {
        Funcionario funcionario = mapper.toEntity(dto);
        
        validarFuncionario(funcionario, false); // Valida a entidade
        
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
        
        return mapper.toResponseDTO(funcionarioSalvo);
    }

    // READ (All): Retorna Lista de DTOs
    public List<FuncionarioResponseDTO> listarTodosFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return mapper.toResponseDTOList(funcionarios);
    }

    // READ (By ID): Retorna um DTO
    public FuncionarioResponseDTO buscarFuncionarioPorId(Long id) {
        Funcionario funcionario = getFuncionarioEntityById(id); // Usa o método interno
        return mapper.toResponseDTO(funcionario);
    }

    // UPDATE: Recebe DTO, retorna DTO
    public FuncionarioResponseDTO atualizarFuncionario(Long id, FuncionarioRequestDTO dto) {
        // 1. Busca a entidade existente
        Funcionario funcionario = getFuncionarioEntityById(id);

        // 2. Prepara a entidade com os novos dados
        funcionario.setNome(dto.nome());
        funcionario.setEmail(dto.email());
        funcionario.setCargo(dto.cargo());
        
        // 3. Valida a entidade atualizada
        validarFuncionario(funcionario, true);
        
        // 4. Salva e retorna o DTO
        Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);
        return mapper.toResponseDTO(funcionarioAtualizado);
    }

    // DELETE (Não muda muito, mas usa o método interno)
    public void deletarFuncionario(Long id) {
        Funcionario funcionario = getFuncionarioEntityById(id);
        funcionarioRepository.delete(funcionario);
    }
}