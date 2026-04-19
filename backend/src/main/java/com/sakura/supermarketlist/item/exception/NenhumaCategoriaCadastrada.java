package com.sakura.supermarketlist.item.exception;

public class NenhumaCategoriaCadastrada  extends RuntimeException{
	
	public NenhumaCategoriaCadastrada() {
		super("Necessário haver pelo menos uma categoria cadastrada.");
	}
	
	

}
