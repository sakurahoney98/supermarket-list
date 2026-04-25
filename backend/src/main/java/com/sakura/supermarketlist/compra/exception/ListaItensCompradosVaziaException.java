package com.sakura.supermarketlist.compra.exception;

public class ListaItensCompradosVaziaException extends RuntimeException {

	public ListaItensCompradosVaziaException() {
		super("Nenhum item foi comprado.");
		
	}
	
	

}
