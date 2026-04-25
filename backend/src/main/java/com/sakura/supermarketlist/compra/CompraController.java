package com.sakura.supermarketlist.compra;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sakura.supermarketlist.compra.dto.CompraRequestDTO;
import com.sakura.supermarketlist.compra.dto.CompraResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/compra")
public class CompraController {
	
	private final CompraService service;

	public CompraController(CompraService service) {
		this.service = service;
	}
	
	@GetMapping("/{dataCompra}")
	public ResponseEntity<List<CompraResponseDTO>> checarData(@PathVariable LocalDate dataCompra) {
		
		List<CompraResponseDTO> response = service.checarDataCompra(dataCompra);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/inserir")
	public ResponseEntity<CompraResponseDTO> inserirCompra(@RequestBody @Valid CompraRequestDTO request) {
		
		CompraResponseDTO response = service.inserirNovaCompra(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping("/unir/{ideCompra}")
	public ResponseEntity<CompraResponseDTO> inserirCompra(@RequestBody @Valid CompraRequestDTO request, @PathVariable Long ideCompra) {
		
		CompraResponseDTO response = service.unirCompra(ideCompra, request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
