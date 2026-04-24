package com.sakura.supermarketlist.listacompra;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sakura.supermarketlist.listacompra.dto.ListaCompraRequestDTO;
import com.sakura.supermarketlist.listacompra.dto.ListaCompraResponseDTO;

@RestController
@RequestMapping("/lista-compras")
public class ListaCompraContorller {
	
	private final ListaCompraService service;
	private final ListaCompraPDFService pdfService;
	
	public ListaCompraContorller(ListaCompraService service, ListaCompraPDFService pdfService) {
		this.service = service;
		this.pdfService = pdfService;
	}
	
	@GetMapping
	public ResponseEntity<List<ListaCompraResponseDTO>> gerarListaDeCompras(){
		List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	@PostMapping ("/pdf")
	public ResponseEntity<byte[]> exportarListaEmPDF(@RequestBody List<ListaCompraRequestDTO> lista){
		if (lista == null || lista.isEmpty()) {
		    return ResponseEntity.badRequest().build();
		}
		
		byte[] response = pdfService.exportarListaEmPDF(lista);
		
		String nomeArquivo = "lista_compras_" + LocalDate.now() + ".pdf";
		
		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"")
	            .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
	            .body(response);
		
	}

}
