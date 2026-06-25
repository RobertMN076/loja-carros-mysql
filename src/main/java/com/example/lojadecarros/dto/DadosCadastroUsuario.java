package com.example.lojadecarros.dto;

import com.example.lojadecarros.model.Role;

public record DadosCadastroUsuario(String username, String password, Role role) {
}