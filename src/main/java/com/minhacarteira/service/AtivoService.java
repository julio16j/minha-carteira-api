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
import com.minhacarteira.model.dto.NovoAporteDTO;
import com.minhacarteira.model.dto.ResultadoAtivoDTO;
import com.minhacarteira.model.entity.Ativo;
import com.minhacarteira.model.enums.TipoAtivo;
import com.minhacarteira.repository.AtivoRepository;

import jakarta.validation.Valid;

@Service
public class AtivoService {

	private final AtivoRepository ativoRepository;
	private final BolsaService bolsaService;

	@Autowired
	public AtivoService(AtivoRepository ativoRepository, BolsaService bolsaService) {
		this.ativoRepository = ativoRepository;
		this.bolsaService = bolsaService;
	}

	public List<ResultadoAtivoDTO> listarTodos() {
		List<Ativo> ativos = ativoRepository.findAll();
		return mapToResultadoAtivoDTO(ativos);
	}

	private List<ResultadoAtivoDTO> mapToResultadoAtivoDTO(List<Ativo> ativos) {
		return ativos.stream().map(ResultadoAtivoDTO::fromEntity).toList();
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
		
		ativoExistente.setTicker(ativoDTO.ticker());
		ativoExistente.setQuantidade(ativoDTO.quantidade());
		ativoExistente.setPrecoMedio(ativoDTO.precoMedio());
		ativoExistente.setNota(ativoDTO.nota());
		ativoExistente.setYield(ativoDTO.yield());
		ativoExistente.setSetor(ativoDTO.setor());
		ativoExistente.setTipoAtivo(ativoDTO.tipoAtivo());

		return ativoRepository.save(ativoExistente);
	}

	public void excluir(Long id) {
		ativoRepository.deleteById(id);
	}

	public List<ResultadoAtivoDTO> listarPorTipo(TipoAtivo tipoAtivo) {
		return mapToResultadoAtivoDTO(ativoRepository.findByTipoAtivo(tipoAtivo));
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
			if (ativo.nota() == null) {
				return null;
			}
			Double percentualNota = (double) ativo.nota() / somaNotas;
			Double precoAtualAtivo = bolsaService.obterPrecoAtivo(ativo.ticker());
			Double valorIdeal = percentualNota * (valorAporte + somaValor);
			Double valorAtual = ativo.quantidade() * precoAtualAtivo;
			Integer novaQuantidade = (int) ((valorIdeal - valorAtual) / precoAtualAtivo);
			Double total = novaQuantidade * ativo.preco();

			return new CalculoAporteDTO(ativo.id(), ativo.ticker(), novaQuantidade, ativo.preco(), total);
		}).filter(calculoAporteDTO -> calculoAporteDTO != null).collect(Collectors.toList());
	}

	public void novoAporte(@Valid NovoAporteDTO novoAporteDTO) {
		Ativo ativoExistente = buscarPorId(novoAporteDTO.id());
		Integer novaQuantidade = ativoExistente.getQuantidade() + novoAporteDTO.quantidade();
		double aporte = novoAporteDTO.preco() * novoAporteDTO.quantidade();
		Double novoPrecoMedio = ((ativoExistente.getPrecoMedio() * ativoExistente.getQuantidade()) + aporte)/novaQuantidade;
		ativoExistente.setQuantidade(novaQuantidade);
		ativoExistente.setPrecoMedio(novoPrecoMedio);
		ativoRepository.save(ativoExistente);
		
	}
}