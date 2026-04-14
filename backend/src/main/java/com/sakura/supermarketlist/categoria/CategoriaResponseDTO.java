package com.sakura.supermarketlist.categoria;

public record CategoriaResponseDTO(
	    Long id,
	    String nome,
	    String corLetra,
	    String corFundo,
	    boolean ativo
	) {}
