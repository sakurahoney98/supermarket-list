package com.sakura.supermarketlist.relatorio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RelatorioGastoItemResponseDTO(
		LocalDate dataCompra,
		String marca,
		BigDecimal valorTotalPago
		) {

}
