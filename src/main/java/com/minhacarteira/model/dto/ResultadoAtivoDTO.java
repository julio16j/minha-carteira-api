package com.minhacarteira.model.dto;

import com.minhacarteira.model.entity.Ativo;
import com.minhacarteira.model.enums.Setor;
import com.minhacarteira.model.enums.TipoAtivo;

public record ResultadoAtivoDTO(Long id, String ticker, Integer quantidade, Double total, Double lucro, Double precoMedio, Double preco, Integer nota, Double yield, Setor setor, TipoAtivo tipoAtivo) {
	public static ResultadoAtivoDTO fromEntity(Ativo ativo) {
		return new ResultadoAtivoDTO(ativo.getId(), ativo.getTicker(), ativo.getQuantidade(), ativo.getQuantidade() * ativo.getPrecoMedio(), 0d, ativo.getPrecoMedio(),
				ativo.getPrecoMedio(), ativo.getNota(), ativo.getYield(), ativo.getSetor(), ativo.getTipoAtivo());
	}
	
	public Ativo toEntity () {
		return new Ativo(id, ticker, quantidade, precoMedio, nota, yield, setor, tipoAtivo );
	}
}