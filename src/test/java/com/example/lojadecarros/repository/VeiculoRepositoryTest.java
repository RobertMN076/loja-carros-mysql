package com.example.lojadecarros.repository;

import com.example.lojadecarros.model.Veiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VeiculoRepositoryTest {

    @Autowired
    private VeiculoRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    // --- 1. SALVAR ---
    @Test
    void deveSalvarVeiculoComSucesso_GoodPath() {
        System.out.println("\n--- [REPOSITORY] Teste: Salvar veículo válido ---");
        Veiculo salvo = repository.save(new Veiculo("Toyota", "Corolla"));
        assertNotNull(salvo.getId());
        System.out.println("[RESULTADO: DEU CERTO] Motivo: O veículo foi inserido no MySQL e o banco gerou um ID automaticamente.");
    }

    @Test
    void deveFalharAoSalvarVeiculoNulo_BadPath() {
        System.out.println("\n--- [REPOSITORY] Teste: Salvar objeto nulo ---");
        assertThrows(Exception.class, () -> repository.save(null));
        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: A infraestrutura JPA barrou a tentativa de salvar um dado nulo, protegendo o banco.");
    }

    // --- 2. BUSCAR POR ID ---
    @Test
    void deveEncontrarVeiculoPorId_GoodPath() {
        System.out.println("\n--- [REPOSITORY] Teste: Buscar ID existente ---");
        Veiculo salvo = repository.save(new Veiculo("Honda", "Fit"));
        Optional<Veiculo> resultado = repository.findById(salvo.getId());
        assertTrue(resultado.isPresent());
        System.out.println("[RESULTADO: DEU CERTO] Motivo: A consulta no banco localizou o registro exato através da chave primária.");
    }

    @Test
    void naoDeveEncontrarVeiculoInexistente_BadPath() {
        System.out.println("\n--- [REPOSITORY] Teste: Buscar ID fantasma ---");
        Optional<Veiculo> resultado = repository.findById(9999L);
        assertFalse(resultado.isPresent());
        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: O banco não encontrou o ID 9999 e retornou vazio sem quebrar o sistema.");
    }

    // --- 3. EXCLUIR ---
    @Test
    void deveDeletarVeiculoComSucesso_GoodPath() {
        System.out.println("\n--- [REPOSITORY] Teste: Deletar veículo existente ---");
        Veiculo salvo = repository.save(new Veiculo("Jeep", "Compass"));
        repository.deleteById(salvo.getId());
        assertFalse(repository.findById(salvo.getId()).isPresent());
        System.out.println("[RESULTADO: DEU CERTO] Motivo: O comando DELETE foi executado e o registro foi apagado do MySQL.");
    }

    @Test
    void deveIgnorarDelecaoDeIdInexistente_BadPath() {
        System.out.println("\n--- [REPOSITORY] Teste: Deletar ID fantasma ---");
        assertDoesNotThrow(() -> repository.deleteById(9999L));
        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: O Spring JPA ignorou a deleção do ID 9999 de forma segura, sem causar falha fatal.");
    }

    // --- 4. LISTAR TODOS ---
    @Test
    void deveListarTodosOsVeiculos_GoodPath() {
        System.out.println("\n--- [REPOSITORY] Teste: Listar tabela populada ---");
        repository.save(new Veiculo("Fiat", "Uno"));
        repository.save(new Veiculo("Ford", "Ka"));
        List<Veiculo> lista = repository.findAll();
        assertEquals(2, lista.size());
        System.out.println("[RESULTADO: DEU CERTO] Motivo: O SELECT retornou perfeitamente os 2 veículos cadastrados no banco.");
    }

    @Test
    void deveRetornarListaVazia_BadPath() {
        System.out.println("\n--- [REPOSITORY] Teste: Listar tabela vazia ---");
        List<Veiculo> lista = repository.findAll();
        assertTrue(lista.isEmpty());
        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: A tabela estava vazia, mas o banco retornou uma lista limpa ao invés de um erro.");
    }
}