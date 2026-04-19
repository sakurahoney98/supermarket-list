package com.sakura.supermarketlist.categoria.exception;

public class CategoriaInexistenteException extends RuntimeException {
	
	public CategoriaInexistenteException() {
		super("Identificador não existe ou a categoria já se encontra inativada."); 
	}

}
