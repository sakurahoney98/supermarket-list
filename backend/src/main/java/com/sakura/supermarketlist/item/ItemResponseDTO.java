package com.sakura.supermarketlist.item;

import java.time.LocalDate;

import com.sakura.supermarketlist.categoria.Categoria;

public record ItemResponseDTO(
		Long id,
		String nome,
		String unidadeMedida,
		Integer quantidadeEstoque,
		Integer limiteCompra,
		LocalDate dataUltimaCompra,
		Categoria categoria,
		Integer duracaoDias,
		boolean indAtivo
		) {}
