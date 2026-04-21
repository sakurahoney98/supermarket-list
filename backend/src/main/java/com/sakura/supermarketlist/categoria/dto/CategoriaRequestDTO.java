package com.sakura.supermarketlist.categoria.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDTO(
	    @NotBlank String nome,
	    @NotBlank String corLetra,
	    @NotBlank String corFundo
	) {}
