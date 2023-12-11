package com.minhacarteira.model.dto;

import com.minhacarteira.model.entity.Caixa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CaixaDTO(
        @NotBlank(message = "O nome não pode estar em branco") String nome,
        @NotNull(message = "O valor não pode ser nulo") @Positive(message = "O valor deve ser positivo") Double valor,
        String rentabilidade,
        String liquidez
) {
	public Caixa toEntity() {
        return new Caixa(null, nome, valor, rentabilidade, liquidez);
    }
}