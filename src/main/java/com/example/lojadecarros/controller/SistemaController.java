package com.example.lojadecarros.controller;

import com.example.lojadecarros.model.Veiculo;
import com.example.lojadecarros.service.SistemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SistemaController {

    private final SistemaService service;

    public SistemaController(SistemaService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String senha) {
        if (service.login(usuario, senha)) {
            return ResponseEntity.ok("Login realizado com sucesso");
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    @PostMapping("/veiculos")
    public Veiculo cadastrar(@RequestBody Veiculo veiculo) {
        return service.cadastrar(veiculo);
    }

    @GetMapping("/veiculos")
    public List<Veiculo> listar() {
        return service.listarTodos();
    }

    @DeleteMapping("/veiculos/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (service.remover(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}