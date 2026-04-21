package com.sakura.supermarketlist.item;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sakura.supermarketlist.common.dto.ExclusaoResponseDTO;

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
	
	
	@PutMapping("/{ideItem}")
	public ResponseEntity<ItemResponseDTO> editarItem(@RequestBody @Valid ItemRequestDTO request, @PathVariable Long ideItem) {
		ItemResponseDTO response = service.editarItem(request, ideItem);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping("/{ideItem}")
	public ResponseEntity<ExclusaoResponseDTO> excluirCategoria(@PathVariable Long ideItem) {
		ExclusaoResponseDTO response = service.excluirItem(ideItem);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
	
	@DeleteMapping
	public ResponseEntity<ExclusaoResponseDTO> excluirCategoria(@RequestBody List<Long> ideItem) {
		ExclusaoResponseDTO response = service.excluirItem(ideItem);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

}
