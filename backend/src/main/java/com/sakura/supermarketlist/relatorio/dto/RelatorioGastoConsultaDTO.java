package com.sakura.supermarketlist.relatorio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RelatorioGastoConsultaDTO(
		LocalDate dataCompra,
		String marca,
		BigDecimal preco,
		Integer quantidade
		) {

}
