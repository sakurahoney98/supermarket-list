package com.sakura.supermarketlist.item.exception;

public class ItemInexistenteException extends RuntimeException {
	
	public ItemInexistenteException(Long ideItem) {
		super("Identificador " + ideItem + " não existe ou o item já se encontra inativado."); 
	}

}