package com.sakura.supermarketlist.listacompra.dto;

import com.sakura.supermarketlist.categoria.dto.CategoriaResponseDTO;

public record ListaCompraResponseDTO(
	    Long ideItem,
	    String nomeItem,
	    String unidadeMedida,
	    CategoriaResponseDTO categoria,
	    Integer quantidadeAtual,
	    Integer quantidadeSugerida,
	    Integer limiteCompra
		) {

}
