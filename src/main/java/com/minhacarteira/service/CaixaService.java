package com.minhacarteira.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.minhacarteira.model.dto.CaixaDTO;
import com.minhacarteira.model.entity.Caixa;
import com.minhacarteira.repository.CaixaRepository;

import jakarta.validation.Valid;

@Service
public class CaixaService {

	@Autowired
	private CaixaRepository caixaRepository;

    public List<Caixa> listarTodos() {
        return caixaRepository.findAll();
    }

    public Caixa buscarPorId(Long id) {
        return caixaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado com o ID: " + id));
    }

    public Caixa salvar(@Valid CaixaDTO caixaDTO) {
    	Caixa caixa = caixaDTO.toEntity();
        return caixaRepository.save(caixa);
    }

    public void excluir(Long id) {
        caixaRepository.deleteById(id);
    }
    
    public Caixa atualizar(Long id, @Valid CaixaDTO caixaDTO) {
        Caixa caixaExistente = buscarPorId(id);
        caixaExistente.setNome(caixaDTO.nome());
        caixaExistente.setValor(caixaDTO.valor());
        caixaExistente.setRentabilidade(caixaDTO.rentabilidade());
        caixaExistente.setLiquidez(caixaDTO.liquidez());
        return caixaRepository.save(caixaExistente);
    }
}