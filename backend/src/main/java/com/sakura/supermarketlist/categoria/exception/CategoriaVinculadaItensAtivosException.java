package com.sakura.supermarketlist.categoria.exception;

import java.util.List;

public class CategoriaVinculadaItensAtivosException extends RuntimeException {
	
	public CategoriaVinculadaItensAtivosException (List<String> itenVinculados) {
		super (itenVinculados.toString());
	}

}
