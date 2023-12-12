package com.minhacarteira.model.dto;

public record CalculoAporteDTO(
        Long id,
        String ticker,
        Integer novaQuantidade,
        Double preco,
        Double total
) {}