package com.sakura.supermarketlist.relatorio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RelatorioConsultaDTO(
		LocalDate dataCompra,
		String marca,
		BigDecimal preco,
		Integer quantidade
		) {

}
