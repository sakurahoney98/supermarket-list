package com.sakura.supermarketlist.compra;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public record CompraRequestDTO(
		@NotNull LocalDate dataCompra,
		@NotNull List<ItemCompraRequestDTO> listaItens
		) {

}
