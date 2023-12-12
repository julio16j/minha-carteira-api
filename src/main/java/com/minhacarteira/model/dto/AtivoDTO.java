package com.minhacarteira.model.dto;

import com.minhacarteira.model.entity.Ativo;
import com.minhacarteira.model.enums.Setor;
import com.minhacarteira.model.enums.TipoAtivo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AtivoDTO(
        @NotBlank(message = "O ticker não pode estar em branco") String ticker,
        @NotNull(message = "A quantidade não pode ser nula") @Positive(message = "A quantidade deve ser positiva") Integer quantidade,
        @NotNull(message = "O preço médio não pode ser nulo") @Positive(message = "O preço médio deve ser positivo") Double precoMedio,
        @NotNull(message = "A nota não pode ser nula") @Positive(message = "A nota deve ser positivo") Integer nota,
        @NotNull(message = "O Dividend yield não pode ser nulo") @Positive(message = "O Dividend yield deve ser positivo") Double dividendYield,
        @NotNull(message = "O setor não pode ser nulo") Setor setor,
        @NotNull(message = "O tipo de ativo não pode ser nulo") TipoAtivo tipoAtivo
) {
	public Ativo toEntity() {
        return new Ativo(
                null,
                ticker,
                quantidade,
                precoMedio,
                nota,
                dividendYield,
                setor,
                tipoAtivo
        );
    }
}