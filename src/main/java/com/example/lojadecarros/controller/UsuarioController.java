package com.example.lojadecarros.controller;

import com.example.lojadecarros.dto.DadosCadastroUsuario;
import com.example.lojadecarros.model.Usuario;
import com.example.lojadecarros.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody DadosCadastroUsuario dados) {
        // Cria o usuário e já criptografa a senha antes de salvar no banco
        Usuario novoUsuario = new Usuario(
                dados.username(),
                passwordEncoder.encode(dados.password()),
                dados.role()
        );
        repository.save(novoUsuario);
        return ResponseEntity.ok("Usuário " + dados.username() + " cadastrado com sucesso como " + dados.role());
    }
}