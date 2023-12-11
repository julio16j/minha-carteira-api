package com.minhacarteira.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhacarteira.model.entity.Caixa;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
}