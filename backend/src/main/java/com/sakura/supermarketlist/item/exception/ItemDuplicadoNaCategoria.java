package com.sakura.supermarketlist.item.exception;

public class ItemDuplicadoNaCategoria extends RuntimeException{
	
	public ItemDuplicadoNaCategoria() {
		super("Já existe um item com o mesmo nome cadastrado nessa categoria.");
	}

}
