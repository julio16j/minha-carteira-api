package com.minhacarteira.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.minhacarteira.model.dto.AtivoDTO;
import com.minhacarteira.model.dto.AtivoPrecoAtualizadoDTO;
import com.minhacarteira.model.dto.CalculoAporteDTO;
import com.minhacarteira.model.entity.Ativo;
import com.minhacarteira.model.enums.TipoAtivo;
import com.minhacarteira.repository.AtivoRepository;

@Service
public class AtivoService {

	private final AtivoRepository ativoRepository;
	private final BolsaService bolsaService;

	@Autowired
	public AtivoService(AtivoRepository ativoRepository, BolsaService bolsaService) {
		this.ativoRepository = ativoRepository;
		this.bolsaService = bolsaService;
	}

	public List<Ativo> listarTodos() {
		return ativoRepository.findAll();
	}

	public Ativo buscarPorId(Long id) {
		return ativoRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ativo não encontrado com o ID: " + id));
	}

	public Ativo salvar(AtivoDTO ativoDTO) {
		Ativo ativo = ativoDTO.toEntity();
		return ativoRepository.save(ativo);
	}

	public Ativo atualizar(Long id, AtivoDTO ativoDTO) {
		Ativo ativoExistente = buscarPorId(id);

		// Atualiza os campos do ativo existente com base no DTO
		ativoExistente.setTicker(ativoDTO.ticker());
		ativoExistente.setQuantidade(ativoDTO.quantidade());
		ativoExistente.setPrecoMedio(ativoDTO.precoMedio());
		ativoExistente.setNota(ativoDTO.nota());
		ativoExistente.setDividendYield(ativoDTO.dividendYield());
		ativoExistente.setSetor(ativoDTO.setor());
		ativoExistente.setTipoAtivo(ativoDTO.tipoAtivo());

		return ativoRepository.save(ativoExistente);
	}

	public void excluir(Long id) {
		ativoRepository.deleteById(id);
	}

	public List<Ativo> listarPorTipo(TipoAtivo tipoAtivo) {
		return ativoRepository.findByTipoAtivo(tipoAtivo);
	}

	public List<CalculoAporteDTO> calcularAporte(Double valorAporte, TipoAtivo tipoAtivo) {
		List<Ativo> ativos = ativoRepository.findByTipoAtivo(tipoAtivo);

		Integer somaNotasAux = 0;
		Double somaValorAux = 0d;

		List<AtivoPrecoAtualizadoDTO> ativosAtualizados = new ArrayList<>();
		
		for (Ativo ativo : ativos) {
		    Double precoAtualAtivo = bolsaService.obterPrecoAtivo(ativo.getTicker());
		    AtivoPrecoAtualizadoDTO ativoPrecoAtualizadoDTO = AtivoPrecoAtualizadoDTO.fromEntity(ativo, precoAtualAtivo);
		    ativosAtualizados.add(ativoPrecoAtualizadoDTO);
		    if (ativo.getNota() != null) {
		    	somaNotasAux += ativo.getNota();
		    }
		    somaValorAux += ativo.getQuantidade() * precoAtualAtivo;
		}
		
		final Integer somaNotas = somaNotasAux;
		final Double somaValor = somaValorAux;
		return ativosAtualizados.stream().map(ativo -> {
			if (ativo.Nota() == null) {
				return null;
			}
			Double percentualNota = (double) ativo.Nota() / somaNotas;
			Double precoAtualAtivo = bolsaService.obterPrecoAtivo(ativo.ticker());
			Double valorIdeal = percentualNota * (valorAporte + somaValor);
			Double valorAtual = ativo.quantidade() * precoAtualAtivo;
			Integer novaQuantidade = (int) ((valorIdeal - valorAtual) / precoAtualAtivo);
			Double total = novaQuantidade * ativo.preco();

			return new CalculoAporteDTO(ativo.id(), ativo.ticker(), novaQuantidade, ativo.preco(), total);
		}).filter(calculoAporteDTO -> calculoAporteDTO != null).collect(Collectors.toList());
	}
}