package com.minhacarteira.model.dto;

import com.minhacarteira.model.entity.Ativo;
import com.minhacarteira.model.enums.TipoAtivo;

public record AtivoPrecoAtualizadoDTO(Long id, String ticker, Integer quantidade, Double precoMedio, Double preco, Integer Nota,
		TipoAtivo tipoAtivo) {
	public static AtivoPrecoAtualizadoDTO fromEntity(Ativo ativo, Double novoPreco) {
		return new AtivoPrecoAtualizadoDTO(ativo.getId(), ativo.getTicker(), ativo.getQuantidade(), ativo.getPrecoMedio(),
				novoPreco, ativo.getNota(), ativo.getTipoAtivo());
	}
}