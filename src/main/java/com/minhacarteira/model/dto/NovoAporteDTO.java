package com.minhacarteira.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NovoAporteDTO(@NotNull(message = "O id não pode ser nulo") Long id,
		@NotNull(message = "A quantidade não pode ser nula") @Positive(message = "A quantidade deve ser positiva") Integer quantidade,
		@NotNull(message = "O preço não pode ser nulo") @Positive(message = "O preço médio deve ser positivo") Double preco) {
}