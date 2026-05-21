package com.example.lojadecarros.service;

import com.example.lojadecarros.model.Veiculo;
import com.example.lojadecarros.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SistemaServiceTest {

    @Autowired
    private SistemaService service;

    @Autowired
    private VeiculoRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    // --- 1. CADASTRAR ---
    @Test
    void deveCadastrarVeiculo_GoodPath() {
        System.out.println("\n--- [SERVICE] Teste: Regra de negócio - Cadastrar válido ---");
        Veiculo salvo = service.cadastrar(new Veiculo("Nissan", "Kicks"));
        assertEquals("Nissan", salvo.getMarca());
        System.out.println("[RESULTADO: DEU CERTO] Motivo: A camada de serviço processou os dados e encaminhou para o banco com sucesso.");
    }

    @Test
    void deveFalharAoCadastrarDadosInvalidos_BadPath() {
        System.out.println("\n--- [SERVICE] Teste: Regra de negócio - Cadastrar inválido ---");
        assertThrows(Exception.class, () -> service.cadastrar(new Veiculo(null, "Civic")));
        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: A camada de serviço identificou a anomalia (marca nula) e bloqueou a ação.");
    }

    // --- 2. BUSCAR POR ID ---
    @Test
    void deveObterVeiculoPorId_GoodPath() {
        System.out.println("\n--- [SERVICE] Teste: Regra de negócio - Buscar ID válido ---");
        Veiculo salvo = repository.save(new Veiculo("BMW", "320i"));
        Optional<Veiculo> resultado = service.buscarPorId(salvo.getId());
        assertTrue(resultado.isPresent());
        System.out.println("[RESULTADO: DEU CERTO] Motivo: O serviço repassou a solicitação de busca e o veículo foi encontrado.");
    }

    @Test
    void deveRetornarVazioAoBuscarIdFalso_BadPath() {
        System.out.println("\n--- [SERVICE] Teste: Regra de negócio - Buscar ID falso ---");
        Optional<Veiculo> resultado = service.buscarPorId(9999L);
        assertFalse(resultado.isPresent());
        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: O serviço processou a ausência do ID corretamente, retornando um Optional vazio.");
    }

    // --- 3. EXCLUIR ---
    @Test
    void deveRemoverVeiculo_GoodPath() {
        System.out.println("\n--- [SERVICE] Teste: Regra de negócio - Remover ID existente ---");
        Veiculo salvo = repository.save(new Veiculo("Hyundai", "HB20"));
        boolean resultado = service.remover(salvo.getId());
        assertTrue(resultado);
        System.out.println("[RESULTADO: DEU CERTO] Motivo: A deleção ocorreu perfeitamente e o serviço confirmou com retorno TRUE.");
    }

    @Test
    void deveFalharAoRemoverIdInexistente_BadPath() {
        System.out.println("\n--- [SERVICE] Teste: Regra de negócio - Remover ID inexistente ---");
        boolean resultado = service.remover(9999L);
        assertFalse(resultado);
        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: O serviço não encontrou o veículo e se defendeu retornando FALSE.");
    }

    // --- 4. LISTAR TODOS ---
    @Test
    void deveObterTodosOsVeiculos_GoodPath() {
        System.out.println("\n--- [SERVICE] Teste: Regra de negócio - Listar veículos ---");
        repository.save(new Veiculo("Volvo", "XC60"));
        List<Veiculo> lista = service.listarTodos();
        assertEquals(1, lista.size());
        System.out.println("[RESULTADO: DEU CERTO] Motivo: O serviço coordenou a listagem corretamente, retornando os itens existentes.");
    }

    @Test
    void deveRetornarListaVazia_BadPath() {
        System.out.println("\n--- [SERVICE] Teste: Regra de negócio - Listar banco vazio ---");
        List<Veiculo> lista = service.listarTodos();
        assertTrue(lista.isEmpty());
        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: O serviço suportou a falta de dados, retornando uma coleção segura (vazia).");
    }
}