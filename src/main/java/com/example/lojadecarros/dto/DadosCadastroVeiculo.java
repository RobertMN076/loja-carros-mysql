package com.example.lojadecarros.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroVeiculo(
        @NotBlank(message = "A marca não pode estar em branco")
        String marca,

        @NotBlank(message = "O modelo não pode estar em branco")
        String modelo
) {
}