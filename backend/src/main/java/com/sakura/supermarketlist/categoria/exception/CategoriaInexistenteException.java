package com.sakura.supermarketlist.categoria.exception;

public class CategoriaInexistenteException extends RuntimeException {
	
	public CategoriaInexistenteException(Long ideCategoria) {
		super("Identificador " + ideCategoria + " não existe ou a categoria já se encontra inativada."); 
	}

}
