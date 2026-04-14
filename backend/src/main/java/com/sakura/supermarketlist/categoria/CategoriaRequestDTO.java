package com.sakura.supermarketlist.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDTO(
	    @NotBlank String nome,
	    @NotBlank String corLetra,
	    @NotBlank String corFundo
	) {}
