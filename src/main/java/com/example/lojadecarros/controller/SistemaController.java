package com.example.lojadecarros.controller;

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
    public ResponseEntity<Veiculo> cadastrar(@Valid @RequestBody Veiculo veiculo) {
        Veiculo salvo = service.cadastrar(veiculo);
        return ResponseEntity.ok(salvo);
    }

    // 2. LISTAR TODOS (GET /api/veiculos)
    @GetMapping
    public ResponseEntity<List<Veiculo>> listarTodos() {
        List<Veiculo> lista = service.listarTodos();
        return ResponseEntity.ok(lista);
    }


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