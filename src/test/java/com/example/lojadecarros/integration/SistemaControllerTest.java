package com.example.lojadecarros.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        repository.deleteAll();
    }

    // --- 1. SALVAR (POST) ---
    @Test
    void deveRetornarStatus200AoSalvar_GoodPath() throws Exception {
        System.out.println("\n--- [CONTROLLER] Teste: Web POST válido ---");
        Veiculo novoCarro = new Veiculo("Honda", "Civic");

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoCarro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("Honda"));

        System.out.println("[RESULTADO: DEU CERTO] Motivo: A API recebeu o JSON válido e devolveu Status 200 (OK).");
    }

    @Test
    void deveRetornarStatus400AoSalvarSemMarca_BadPath() throws Exception {
        System.out.println("\n--- [CONTROLLER] Teste: Web POST inválido (sem marca) ---");
        Veiculo carroInvalido = new Veiculo(null, "Civic");

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carroInvalido)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: A API barrou os dados incompletos na porta, retornando Status 400 (Bad Request).");
    }

    // --- 2. BUSCAR POR ID (GET) ---
    @Test
    void deveRetornarStatus200AoBuscarId_GoodPath() throws Exception {
        System.out.println("\n--- [CONTROLLER] Teste: Web GET por ID válido ---");
        Veiculo carroSalvo = repository.save(new Veiculo("Ford", "Fiesta"));

        mockMvc.perform(get("/api/veiculos/" + carroSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("Ford"));

        System.out.println("[RESULTADO: DEU CERTO] Motivo: A API processou a URL, achou o carro e devolveu Status 200 (OK).");
    }

    @Test
    void deveRetornarStatus404AoBuscarIdFalso_BadPath() throws Exception {
        System.out.println("\n--- [CONTROLLER] Teste: Web GET por ID falso ---");
        mockMvc.perform(get("/api/veiculos/9999"))
                .andDo(print())
                .andExpect(status().isNotFound());

        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: A API percebeu que o recurso não existe e devolveu Status 404 (Not Found) padrão web.");
    }

    // --- 3. EXCLUIR (DELETE) ---
    @Test
    void deveRetornarStatus200AoExcluir_GoodPath() throws Exception {
        System.out.println("\n--- [CONTROLLER] Teste: Web DELETE em ID existente ---");
        Veiculo carroSalvo = repository.save(new Veiculo("Chevrolet", "Onix"));

        mockMvc.perform(delete("/api/veiculos/" + carroSalvo.getId()))
                .andExpect(status().isOk());

        System.out.println("[RESULTADO: DEU CERTO] Motivo: A API autorizou a deleção no banco e respondeu com Status 200 (OK).");
    }

    @Test
    void deveRetornarStatus404AoExcluirIdFalso_BadPath() throws Exception {
        System.out.println("\n--- [CONTROLLER] Teste: Web DELETE em ID falso ---");
        mockMvc.perform(delete("/api/veiculos/9999"))
                .andDo(print())
                .andExpect(status().isNotFound());

        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: A API bloqueou a deleção do nada e avisou o cliente com Status 404 (Not Found).");
    }

    // --- 4. LISTAR TODOS (GET) ---
    @Test
    void deveRetornarStatus200ComLista_GoodPath() throws Exception {
        System.out.println("\n--- [CONTROLLER] Teste: Web GET listagem populada ---");
        repository.save(new Veiculo("Audi", "A3"));

        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        System.out.println("[RESULTADO: DEU CERTO] Motivo: A API montou o vetor JSON corretamente e despachou com Status 200 (OK).");
    }

    @Test
    void deveRetornarStatus200ComListaVazia_BadPath() throws Exception {
        System.out.println("\n--- [CONTROLLER] Teste: Web GET listagem vazia ---");
        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        System.out.println("[RESULTADO: DEU CERTO O ERRO] Motivo: A API não estourou erro 500. Ela devolveu um array vazio com Status 200, protegendo o frontend.");
    }
}