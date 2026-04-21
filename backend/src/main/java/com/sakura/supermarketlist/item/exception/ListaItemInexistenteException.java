package com.sakura.supermarketlist.item.exception;

import java.util.List;

public class ListaItemInexistenteException extends RuntimeException {
	
	public ListaItemInexistenteException(List<Long> idesItem) {
		super("Identificador " + idesItem.toString() + " não existe ou a categoria já se encontra inativada."); 
	}

}
