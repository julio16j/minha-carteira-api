package com.minhacarteira.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minhacarteira.model.dto.AtivoPrecoAtualizadoDTO;
import com.minhacarteira.model.dto.DashboardDTO;
import com.minhacarteira.model.enums.TipoAtivo;

@Service
public class DashboardService {

	private final AtivoService ativoService;
	private final CaixaService caixaService;
	private final BolsaService bolsaService;

	@Autowired
	public DashboardService(AtivoService ativoService, BolsaService bolsaService, CaixaService caixaService) {
		this.ativoService = ativoService;
		this.bolsaService = bolsaService;
		this.caixaService = caixaService;
	}

	public DashboardDTO obterDashboardDTO() {
		DashboardDTO dashboardDTO = new DashboardDTO();
		computarAtivos(dashboardDTO);
		computarCaixas(dashboardDTO);
		dashboardDTO.setLucroNominal(dashboardDTO.getTotalBruto() - dashboardDTO.getTotalAplicado());
		if (dashboardDTO.getTotalAplicado() > 0) {
			dashboardDTO.setLucroPercentual(
					(dashboardDTO.getTotalBruto() - dashboardDTO.getTotalAplicado()) / dashboardDTO.getTotalAplicado());
		}
		return dashboardDTO;
	}

	private void computarAtivos(DashboardDTO dashboardDTO) {
		ativoService.listarTodos().stream().forEach(ativo -> {
			AtivoPrecoAtualizadoDTO ativoAtualizado = AtivoPrecoAtualizadoDTO.fromEntity(ativo.toEntity(),
					bolsaService.obterPrecoAtivo(ativo.ticker()));
			dashboardDTO.setTotalAplicado(dashboardDTO.getTotalAplicado() + (ativoAtualizado.quantidade() * ativoAtualizado.precoMedio()));
			dashboardDTO.setTotalBruto(dashboardDTO.getTotalBruto() + (ativoAtualizado.quantidade() *  ativoAtualizado.preco()));
			if (TipoAtivo.ACAO.equals(ativoAtualizado.tipoAtivo())) {
				dashboardDTO.setTotalAcoes(dashboardDTO.getTotalAcoes() + (ativoAtualizado.quantidade() * ativoAtualizado.preco()));
			} else {
				dashboardDTO.setTotalFiis(dashboardDTO.getTotalFiis() + (ativoAtualizado.quantidade() * ativoAtualizado.preco()));
			}
		});
	}

	private void computarCaixas(DashboardDTO dashboardDTO) {
		caixaService.listarTodos().stream().forEach(caixa -> {
			dashboardDTO.setTotalAplicado(dashboardDTO.getTotalAplicado() + caixa.getValor());
			dashboardDTO.setTotalBruto(dashboardDTO.getTotalBruto() + caixa.getValor());
			dashboardDTO.setTotalCaixa(dashboardDTO.getTotalCaixa() + caixa.getValor());
		});
	}

}