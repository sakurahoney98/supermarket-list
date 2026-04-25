package com.sakura.supermarketlist.relatorio.dto;

import java.math.BigDecimal;

public record RelatorioMensalResponseDTO(
		String nomeItem,
		String marca,
		BigDecimal preco,
		Integer quantidade
		) {

}
