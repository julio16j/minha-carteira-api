package com.minhacarteira.model.entity;

import com.minhacarteira.model.enums.Setor;
import com.minhacarteira.model.enums.TipoAtivo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ativo {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Double precoMedio;
    
    @Column(nullable = false)
    private Integer nota;
    
    @Column(nullable = false)
    private Double dividendYield;

    @Enumerated(EnumType.STRING)
    private Setor setor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ativo")
    private TipoAtivo tipoAtivo;
}