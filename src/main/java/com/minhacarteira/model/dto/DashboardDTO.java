package com.minhacarteira.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashboardDTO {
    private Double totalAplicado = 0.0;
    private Double totalBruto = 0.0;
    private Double lucroNominal = 0.0;
    private Double lucroPercentual = 0.0;
    private Double totalAcoes = 0.0;
    private Double totalFiis = 0.0;
    private Double totalCaixa = 0.0;
}