package com.sakura.supermarketlist.listacompra.dto;

import com.sakura.supermarketlist.categoria.dto.CategoriaResponseDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ListaCompraRequestDTO(
		@NotNull Long ideItem,
		@NotBlank String nome,
		@NotNull CategoriaResponseDTO categoria,
		@NotBlank String unidadeMedida,
		@NotNull Integer quantidadeCompra
		) {

}
