package com.sakura.supermarketlist.categoria;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	private final CategoriaService service;

	public CategoriaController(CategoriaService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<CategoriaResponseDTO> cadastrarCategoria(@RequestBody @Valid CategoriaRequestDTO request) {
		CategoriaResponseDTO response = service.cadastrarCategoria(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/{ideCategoria}")
	public ResponseEntity<Void> excluirCategoria(@PathVariable Long ideCategoria) {
		service.excluirCategoria(ideCategoria);
		return ResponseEntity.noContent().build();

	}
	
	@GetMapping("/ordenar-por-nome")
	public ResponseEntity<List<CategoriaResponseDTO>> exibirCategoriasPorNome() {
		
		return ResponseEntity.ok(service.buscarTodasCategoriasAtivasOrdenadasPorNome());
		

	}
	
	@GetMapping("/ordenar-por-data")
	public ResponseEntity<List<CategoriaResponseDTO>> exibirCategoriasPorData() {
		
		return ResponseEntity.ok(service.buscarTodasCategoriasAtivasOrdenadasPorDataCriacao());
		
	}
	
	@GetMapping("/filtro")
	public ResponseEntity<List<CategoriaResponseDTO>> filtrarCategorias(@RequestParam String nome) {
		
		return ResponseEntity.ok(service.filtrarCategoriasPorNome(nome));
		

	}
	
	
}