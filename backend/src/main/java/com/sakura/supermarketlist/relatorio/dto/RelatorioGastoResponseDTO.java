package com.sakura.supermarketlist.relatorio.dto;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioGastoResponseDTO(
		BigDecimal gastoTotal,
		List<RelatorioGastoItemResponseDTO> historico
		) {

}
