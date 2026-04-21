package com.sakura.supermarketlist.categoria.exception;

import java.util.List;

public class ListaCategoriaInexistenteException extends RuntimeException {
	
	public ListaCategoriaInexistenteException(List<Long> idesCategoria) {
		super("Identificador " + idesCategoria.toString() + " não existe ou a categoria já se encontra inativada."); 
	}

}
