package com.minhacarteira.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.minhacarteira.model.dto.CaixaDTO;
import com.minhacarteira.model.entity.Caixa;
import com.minhacarteira.service.CaixaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/caixas")
public class CaixaController {

    private final CaixaService caixaService;

    @Autowired
    public CaixaController(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Caixa> caixas = caixaService.listarTodos();
            return ResponseEntity.ok(caixas);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
        	Caixa caixa = caixaService.buscarPorId(id);
            return ResponseEntity.ok(caixa);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody CaixaDTO caixaDTO) {
        try {
            Caixa caixaSalvo = caixaService.salvar(caixaDTO);
            return ResponseEntity.ok(caixaSalvo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody CaixaDTO caixaDTO) {
        try {
            Caixa caixaAtualizado = caixaService.atualizar(id, caixaDTO);
            return ResponseEntity.ok(caixaAtualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            caixaService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }
}