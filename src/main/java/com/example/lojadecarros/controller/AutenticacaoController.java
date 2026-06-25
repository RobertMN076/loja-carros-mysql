package com.example.lojadecarros.controller;

import com.example.lojadecarros.dto.DadosAutenticacao;
import com.example.lojadecarros.dto.DadosTokenJWT;
import com.example.lojadecarros.model.Usuario;
import com.example.lojadecarros.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        // Monta o token do Spring Security com base no JSON recebido
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.username(), dados.password());

        // Dispara a validação (vai chamar nosso AutenticacaoService por debaixo dos panos)
        var authentication = manager.authenticate(authenticationToken);

        // Gera o JWT
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // Devolve o JWT no corpo da resposta
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}