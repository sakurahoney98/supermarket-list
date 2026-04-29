package com.sakura.supermarketlist.item;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sakura.supermarketlist.common.dto.ExclusaoResponseDTO;
import com.sakura.supermarketlist.item.dto.AtualizacaoEstoqueRequestDTO;
import com.sakura.supermarketlist.item.dto.DashboardResponseDTO;
import com.sakura.supermarketlist.item.dto.ItemRequestDTO;
import com.sakura.supermarketlist.item.dto.ItemResponseDTO;

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
	
	@PutMapping("/estoque")
	public ResponseEntity<List<ItemResponseDTO>> atualizarEstoque(@RequestBody @Valid List<AtualizacaoEstoqueRequestDTO> request) {
		List<ItemResponseDTO> response = service.atualizarEstoque(request);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping("/{ideItem}")
	public ResponseEntity<ExclusaoResponseDTO> excluirItem(@PathVariable Long ideItem) {
		ExclusaoResponseDTO response = service.excluirItem(ideItem);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
	
	@DeleteMapping
	public ResponseEntity<ExclusaoResponseDTO> excluirItens(@RequestParam List<Long> ids) {
	    ExclusaoResponseDTO response = service.excluirItem(ids);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<List<ItemResponseDTO>> buscarItens(
	        @RequestParam(required = false) String nome,
	        @RequestParam(required = false) Long ideCategoria
	) {

	    List<ItemResponseDTO> response;

	    if (nome != null && ideCategoria != null) {
	        response = service.filtrarItensAtivosNacategoriaPorNome(ideCategoria, nome);
	        
	    } else if (nome != null) {
	        response = service.filtrarItensAtivosPorNome(nome);
	        
	    } else if (ideCategoria != null) {
	        response = service.filtrarItensAtivosPorCategoria(ideCategoria);
	        
	    } else {
	        response = service.buscarTodosItensAtivosOrdenadosPorCategoriaENome();
	    }

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/ordenado-por-data")
	public ResponseEntity<List<ItemResponseDTO>> exibirItensPorData() {
		List<ItemResponseDTO> response = service.buscarTodosItensAtivosOrdenadosPorDataCriacao();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/{ideItem}")
	public ResponseEntity<ItemResponseDTO> exibirDetalhesDoItem(@PathVariable Long ideItem) {
		ItemResponseDTO response = service.buscarItemPorId(ideItem);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/dashboard")
	public ResponseEntity<DashboardResponseDTO> capturarIndicadores() {
		DashboardResponseDTO response = service.capturarIndicadores();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	


}
