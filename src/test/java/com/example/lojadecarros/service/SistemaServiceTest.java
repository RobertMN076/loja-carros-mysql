package com.example.lojadecarros.service;

import com.example.lojadecarros.model.Veiculo;
import com.example.lojadecarros.repository.VeiculoRepository;
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

@ExtendWith(MockitoExtension.class)
class SistemaServiceTest {

    @Mock
    private VeiculoRepository repository;

    @InjectMocks
    private SistemaService service;

    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        veiculo = new Veiculo("Toyota", "Corolla");
        veiculo.setId(1L);
    }

    @Test
    void deveCadastrarVeiculoComSucesso() {
        when(repository.save(any(Veiculo.class))).thenReturn(veiculo);
        Veiculo salvo = service.cadastrar(new Veiculo("Toyota", "Corolla"));
        assertNotNull(salvo);
        assertEquals("Toyota", salvo.getMarca());
    }

    @Test
    void deveBuscarVeiculoPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(veiculo));
        Optional<Veiculo> encontrado = service.buscarPorId(1L);
        assertTrue(encontrado.isPresent());
        assertEquals(1L, encontrado.get().getId());
    }

    @Test
    void deveRemoverVeiculoComSucesso() {
        when(repository.existsById(1L)).thenReturn(true);
        boolean removido = service.remover(1L);
        assertTrue(removido);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deveRealizarLoginComSucesso() {
        boolean sucesso = service.login("admin", "1234");
        assertTrue(sucesso);
    }

    @Test
    void deveFalharLoginComDadosIncorretos() {
        boolean falha = service.login("errado", "0000");
        assertFalse(falha);
    }
}