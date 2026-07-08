package com.example.lojadecarros.controller;

import com.example.lojadecarros.dto.DadosCadastroVeiculo;
import com.example.lojadecarros.model.Veiculo;
import com.example.lojadecarros.service.SistemaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
public class SistemaController {

    @Autowired
    private SistemaService service;

    // 1. CADASTRAR (POST /api/veiculos)
    @PostMapping
    public ResponseEntity<Veiculo> cadastrar(@Valid @RequestBody DadosCadastroVeiculo dados) {
        // Converte o DTO para a Entidade antes de salvar
        Veiculo novoVeiculo = new Veiculo(dados.marca(), dados.modelo());
        Veiculo salvo = service.cadastrar(novoVeiculo);

        return ResponseEntity.ok(salvo);
    }

    // 2. LISTAR TODOS (GET /api/veiculos)
    @GetMapping
    public ResponseEntity<List<Veiculo>> listarTodos() {
        List<Veiculo> lista = service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // 3. BUSCAR POR ID (GET /api/veiculos/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. EXCLUIR (DELETE /api/veiculos/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        boolean removido = service.remover(id);
        if (removido) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}