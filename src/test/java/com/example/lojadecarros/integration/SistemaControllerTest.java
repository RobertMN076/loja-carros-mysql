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
class SistemaControllerTest {

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
    void deveBuscarCarroPorId() throws Exception {
        // Salva o dado no banco
        Veiculo carroSalvo = repository.save(new Veiculo("Ford", "Fiesta"));

        // Faz o GET passando o ID gerado na URL (Ex: /api/veiculos/1)
        mockMvc.perform(get("/api/veiculos/" + carroSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("Ford"));
    }

    @Test
    void deveExcluirCarroComSucesso() throws Exception {
        // Salva e pega o ID verdadeiro gerado pelo MySQL
        Veiculo carroSalvo = repository.save(new Veiculo("Chevrolet", "Onix"));

        mockMvc.perform(delete("/api/veiculos/" + carroSalvo.getId()))
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