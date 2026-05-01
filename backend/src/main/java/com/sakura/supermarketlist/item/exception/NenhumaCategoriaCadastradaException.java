package com.sakura.supermarketlist.item.exception;

public class NenhumaCategoriaCadastradaException  extends RuntimeException{
	
	public NenhumaCategoriaCadastradaException() {
		super("Necessário haver pelo menos uma categoria cadastrada.");
	}
	
	

}
