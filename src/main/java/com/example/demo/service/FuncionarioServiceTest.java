package com.example.demo.service;

import com
.example.demo.dto.FuncionarioRequestDTO;
import com.example.demo.dto.FuncionarioResponseDTO;
import com.example.demo.exception.FuncionarioNotFoundException;
import com.example.demo.mapper.FuncionarioMapper;
import com.example.demo.model.Funcionario;
import com.example.demo.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Diz ao JUnit 5 para usar a extensão do Mockito
@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    // Cria "falsificações" (mocks) das dependências
    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private FuncionarioMapper mapper;

    // Injeta os mocks acima no nosso service (o alvo do teste)
    @InjectMocks
    private FuncionarioService funcionarioService;

    // Variáveis de setup para usar nos testes
    private Funcionario funcionario;
    private FuncionarioRequestDTO requestDTO;
    private FuncionarioResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Este método 'setUp' corre ANTES de cada teste
        // É útil para configurar dados comuns
        requestDTO = new FuncionarioRequestDTO("Kevin", "kevin@email.com", "Dev");
        
        funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Kevin");
        funcionario.setEmail("kevin@email.com");
        funcionario.setCargo("Dev");

        responseDTO = new FuncionarioResponseDTO(1L, "Kevin", "kevin@email.com", "Dev");
    }

    // --- Início dos Testes ---

    @Test
    void testCriarFuncionario_Successo() {
        // GIVEN (Dado): Configuramos o comportamento dos mocks
        // Quando o mapper for chamado para converter DTO para Entidade, retorne 'funcionario'
        when(mapper.toEntity(any(FuncionarioRequestDTO.class))).thenReturn(funcionario);
        // Quando o repositório salvar QUALQUER Funcionario, retorne 'funcionario'
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);
        // Quando o mapper for chamado para converter Entidade para DTO, retorne 'responseDTO'
        when(mapper.toResponseDTO(any(Funcionario.class))).thenReturn(responseDTO);

        // WHEN (Quando): Executamos o método que queremos testar
        FuncionarioResponseDTO resultado = funcionarioService.criarFuncionario(requestDTO);

        // THEN (Então): Verificamos se o resultado está correto
        assertNotNull(resultado); // O resultado não pode ser nulo
        assertEquals("Kevin", resultado.nome()); // O nome deve ser "Kevin"
        assertEquals(1L, resultado.id()); // O ID deve ser 1
        
        // Verifica se o repositório foi chamado 1 vez
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class)); 
    }

    @Test
    void testCriarFuncionario_EmailJaExiste() {
        // GIVEN (Dado): Um e-mail que já existe
        when(mapper.toEntity(any(FuncionarioRequestDTO.class))).thenReturn(funcionario);
        // Simula que o repositório ENCONTROU um e-mail duplicado
        when(funcionarioRepository.findByEmail("kevin@email.com")).thenReturn(Optional.of(funcionario));

        // WHEN (Quando) & THEN (Então):
        // Verificamos se a exceção correta é lançada
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> funcionarioService.criarFuncionario(requestDTO) // A ação que deve falhar
        );

        // Verificamos a mensagem da exceção
        assertEquals("O e-mail informado já está em uso.", exception.getMessage());
        
        // Garante que o método 'save' NUNCA foi chamado
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    void testBuscarFuncionarioPorId_Successo() {
        // GIVEN (Dado): O repositório encontra o funcionário
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(mapper.toResponseDTO(funcionario)).thenReturn(responseDTO);

        // WHEN (Quando): Buscamos
        FuncionarioResponseDTO resultado = funcionarioService.buscarFuncionarioPorId(1L);

        // THEN (Então): Verificamos
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
    }

    @Test
    void testBuscarFuncionarioPorId_NaoEncontrado() {
        // GIVEN (Dado): O repositório NÃO encontra o funcionário
        when(funcionarioRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN (Quando) & THEN (Então):
        // Verificamos se a nossa exceção customizada é lançada
        FuncionarioNotFoundException exception = assertThrows(
            FuncionarioNotFoundException.class,
            () -> funcionarioService.buscarFuncionarioPorId(99L)
        );

        assertEquals("Funcionário não encontrado com o ID: 99", exception.getMessage());
    }
}