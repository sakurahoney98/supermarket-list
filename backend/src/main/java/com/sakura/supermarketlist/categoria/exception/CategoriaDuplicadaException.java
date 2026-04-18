package com.sakura.supermarketlist.categoria.exception;

public class CategoriaDuplicadaException extends RuntimeException {
	
	public CategoriaDuplicadaException (String nome) {
		super ("Já existe uma categoria ativa com o nome " + nome);
	}

}
