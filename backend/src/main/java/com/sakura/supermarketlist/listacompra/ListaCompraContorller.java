package com.sakura.supermarketlist.listacompra;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListaCompraContorller {
	
	private final ListaCompraService service;
	
	public ListaCompraContorller(ListaCompraService service) {
		this.service = service;
	}
	
	@GetMapping ("/lista")
	public ResponseEntity<List<ListaCompraResponseDTO>> gerarListaDeCompras(){
		List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}

}
