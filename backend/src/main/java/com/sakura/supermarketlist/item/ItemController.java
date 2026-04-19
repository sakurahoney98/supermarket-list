package com.sakura.supermarketlist.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/itens")
public class ItemController {
	
	private final ItemService service;

	public ItemController(ItemService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<ItemResponseDTO> cadastrarItem(@RequestBody @Valid ItemRequestDTO request) {
		ItemResponseDTO response = service.cadastrarItem(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
