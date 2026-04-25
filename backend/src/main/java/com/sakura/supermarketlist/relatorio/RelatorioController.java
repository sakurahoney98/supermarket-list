package com.sakura.supermarketlist.relatorio;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {
	
	
	RelatorioService service;

	public RelatorioController(RelatorioService service) {
		this.service = service;
	}
	
	@GetMapping("/mensal")
	public ResponseEntity<List<RelatorioMensalResponseDTO>> gerarRelatorioMensal(@RequestParam Integer ano, @RequestParam Integer mes){
		
		List<RelatorioMensalResponseDTO> response = service.relatorioItensCompradosNoMes(ano, mes);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	

}
