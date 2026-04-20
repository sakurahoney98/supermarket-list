package com.sakura.supermarketlist.item.exception;

public class ItemInexistenteException extends RuntimeException {
	
	public ItemInexistenteException() {
		super("Identificador não existe ou o item já se encontra inativado."); 
	}

}