package com.minhacarteira.controller;

import java.util.Collections;
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

import com.minhacarteira.model.dto.AtivoDTO;
import com.minhacarteira.model.dto.CalculoAporteDTO;
import com.minhacarteira.model.dto.ResultadoAtivoDTO;
import com.minhacarteira.model.entity.Ativo;
import com.minhacarteira.model.enums.TipoAtivo;
import com.minhacarteira.service.AtivoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ativo")
public class AtivoController {

    private final AtivoService ativoService;

    @Autowired
    public AtivoController(AtivoService ativoService) {
        this.ativoService = ativoService;
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<ResultadoAtivoDTO> ativos = ativoService.listarTodos();
            return ResponseEntity.ok(ativos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }
    
    @GetMapping("/tipo-ativo/{tipoAtivo}")
    public ResponseEntity<?> listarPorTipoAtivo(@PathVariable TipoAtivo tipoAtivo) {
        try {
            List<ResultadoAtivoDTO> ativos = ativoService.listarPorTipo(tipoAtivo);
            return ResponseEntity.ok(ativos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Ativo ativo = ativoService.buscarPorId(id);
            return ResponseEntity.ok(ResultadoAtivoDTO.fromEntity(ativo));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody AtivoDTO ativoDTO) {
        try {
            Ativo ativoSalvo = ativoService.salvar(ativoDTO);
            return ResponseEntity.ok(ativoSalvo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AtivoDTO ativoDTO) {
        try {
            Ativo ativoAtualizado = ativoService.atualizar(id, ativoDTO);
            return ResponseEntity.ok(ativoAtualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            ativoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }
    
    @GetMapping("/calcular-aporte/{valorAporte}/{tipoAtivo}")
    public ResponseEntity<?> calcularAporte(
            @PathVariable Double valorAporte,
            @PathVariable TipoAtivo tipoAtivo) {
        try {
            List<CalculoAporteDTO> resultado = ativoService.calcularAporte(valorAporte, tipoAtivo);
            if (resultado != null && !resultado.isEmpty()) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }
}