package com.minhacarteira.model.dto;

import com.minhacarteira.model.entity.Ativo;
import com.minhacarteira.model.enums.TipoAtivo;

public record AtivoPrecoAtualizadoDTO(String ticker, Integer quantidade, Double precoMedio, Double preco,
		TipoAtivo tipoAtivo) {
	public static AtivoPrecoAtualizadoDTO fromEntity(Ativo ativo, Double novoPreco) {
		return new AtivoPrecoAtualizadoDTO(ativo.getTicker(), ativo.getQuantidade(), ativo.getPrecoMedio(),
				novoPreco, ativo.getTipoAtivo());
	}
}