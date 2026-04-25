package com.sakura.supermarketlist.compra.exception;

public class CompraInexistenteException extends RuntimeException{

	public CompraInexistenteException(Long ideCompra) {
		super("O identificador " + ideCompra + " não existe");
		
	}
	
	

}
