package com.sakura.supermarketlist.categoria.dto;

public record CategoriaResponseDTO(
	    Long id,
	    String nome,
	    String corLetra,
	    String corFundo,
	    boolean ativo
	) {}
