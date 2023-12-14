package com.minhacarteira.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caixa {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caixa_seq")
    @SequenceGenerator(name = "caixa_seq", sequenceName = "caixa_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double valor;

    private String rentabilidade;

    private String liquidez;
}