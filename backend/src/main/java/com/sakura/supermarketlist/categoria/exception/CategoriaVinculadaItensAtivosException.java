package com.sakura.supermarketlist.categoria.exception;

import java.util.List;

public class CategoriaVinculadaItensAtivosException extends RuntimeException {
	
	public CategoriaVinculadaItensAtivosException (List<String> itenVinculados) {
		super ("Antes de realizar a exclusão você precisa modificar a categoria do(s) seguinte(s) item(ns):\n" + itenVinculados.toString());
	}

}
