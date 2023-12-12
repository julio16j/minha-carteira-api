package com.minhacarteira.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.minhacarteira.model.dto.AtivoDTO;
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
        return ativoRepository.findById(id)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ativo não encontrado com o ID: " + id));
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

        Integer somaNotas = ativos.stream()
                .map(Ativo::getNota)
                .filter(nota -> nota != null)
                .reduce(Integer::sum)
                .orElse(0);
        
        return ativos.stream()
                .map(ativo -> {
                    if (ativo.getNota() == null) {
                        return null;
                    }
                    Double percentualNota = (double) ativo.getNota() / somaNotas;
                    Double precoAtualAtivo = bolsaService.obterPrecoAtivo(ativo.getTicker());
                    Integer novaQuantidade = (int) Math.round(percentualNota * valorAporte / precoAtualAtivo);
                    Double total = novaQuantidade * ativo.getPrecoMedio();

                    return new CalculoAporteDTO(ativo.getId(), ativo.getTicker(), novaQuantidade,
                            ativo.getPrecoMedio(), total);
                })
                .filter(calculoAporteDTO -> calculoAporteDTO != null)
                .collect(Collectors.toList());
    }
}