package com.minhacarteira.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhacarteira.model.entity.Ativo;
import com.minhacarteira.model.enums.TipoAtivo;

public interface AtivoRepository extends JpaRepository<Ativo, Long> {
	List<Ativo> findByTipoAtivo(TipoAtivo tipoAtivo);
}