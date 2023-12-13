package com.minhacarteira.model.dto;

public record CalculoAporteDTO(
        Long id,
        String ticker,
        Integer quantidadeNova,
        Double preco,
        Double total
) {}