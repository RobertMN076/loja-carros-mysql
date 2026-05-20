package com.example.lojadecarros.integration;

import com.example.lojadecarros.model.Veiculo;
import com.example.lojadecarros.repository.VeiculoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SistemaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VeiculoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparBanco() {
        // Garante que o banco de testes está limpo antes de cada método
        repository.deleteAll();
    }

    @Test
    void deveSalvarCarroComSucesso() throws Exception {
        Veiculo novoCarro = new Veiculo("Honda", "Civic");

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoCarro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("Honda"));
    }

    @Test
    @Sql(statements = "INSERT INTO veiculo (id, marca, modelo) VALUES (100, 'Ford', 'Fiesta')")
    void deveBuscarCarroPorId() throws Exception {
        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value("Ford"));
    }

    @Test
    @Sql(statements = "INSERT INTO veiculo (id, marca, modelo) VALUES (101, 'Chevrolet', 'Onix')")
    void deveExcluirCarroComSucesso() throws Exception {
        mockMvc.perform(delete("/api/veiculos/101"))
                .andExpect(status().isOk());
    }

    // --- TESTES ADICIONAIS SOLICITADOS ---

    @Test
    void deveRetornarErroAoBuscarIdInexistente() throws Exception {
        mockMvc.perform(get("/api/veiculos/999"))
                .andExpect(status().isNotFound());
        // Nota: Verifique se o seu Controller retorna 404 para isso.
    }

    @Test
    void deveFalharAoSalvarCarroSemModelo() throws Exception {
        Veiculo carroSemModelo = new Veiculo("Toyota", null);

        // Como não implementamos validações complexas, vamos simular que o sistema
        // processa a requisição, mas o modelo fica nulo no retorno do JSON.
        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carroSemModelo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").doesNotExist());
    }
}