package com.sakura.supermarketlist.item.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemRequestDTO(
		@NotBlank String nome, 
		String unidadeMedida, 
		@NotNull Integer quantidadeEstoque,
		@NotNull Integer limiteCompra, 
		@NotNull LocalDate dataUltimaCompra, 
		@NotNull Long categoria,
		@NotNull Integer duracaoDias) {}
