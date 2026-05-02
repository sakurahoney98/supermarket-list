package com.sakura.supermarketlist.sugestao;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sugestao")
public class SugestaoController {
	
	private final SugestaoService service;

	public SugestaoController(SugestaoService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<SugestaoResponseDTO>> buscarSugestoes(){
		
		List<SugestaoResponseDTO> response = service.buscarSugestoesAtivas();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}

}
