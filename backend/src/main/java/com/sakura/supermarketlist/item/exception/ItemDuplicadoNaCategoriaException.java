package com.sakura.supermarketlist.item.exception;

public class ItemDuplicadoNaCategoriaException extends RuntimeException{
	
	public ItemDuplicadoNaCategoriaException() {
		super("Já existe um item com o mesmo nome cadastrado nessa categoria.");
	}

}
