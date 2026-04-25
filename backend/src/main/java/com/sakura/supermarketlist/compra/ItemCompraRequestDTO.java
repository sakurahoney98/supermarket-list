package com.sakura.supermarketlist.compra;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record ItemCompraRequestDTO(
		@NotNull Long ideItem,
		@NotNull Integer quantidadeComprada,
		BigDecimal valor,
		String marca
		) {

}
