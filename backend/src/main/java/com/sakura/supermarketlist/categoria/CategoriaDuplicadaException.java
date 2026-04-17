package com.sakura.supermarketlist.categoria;

public class CategoriaDuplicadaException extends RuntimeException {
	
	public CategoriaDuplicadaException (String nome) {
		super ("Já existe uma categoria ativa com o nome " + nome);
	}

}
