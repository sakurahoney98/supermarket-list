package com.sakura.supermarketlist.sugestao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SugestaoService {
	
	@Autowired
	SugestaoRepository repository;
	
	public List<SugestaoResponseDTO> buscarSugestoesAtivas() {
		
		List<Sugestao> lista = repository.findByIndAtivoTrue();
		
		return montarListaResposta(lista);
		
	}
	
	private List<SugestaoResponseDTO> montarListaResposta(List<Sugestao> lista){
		
		List<SugestaoResponseDTO> listaFinal = new ArrayList<SugestaoResponseDTO>();
		
		for (Sugestao sugestao : lista) {
			SugestaoResponseDTO objeto = conversaoEntidadeParaDTO(sugestao);
			
			listaFinal.add(objeto);
		}
		
		return listaFinal;
		
	}
	
	private SugestaoResponseDTO conversaoEntidadeParaDTO(Sugestao sugestao) {
		return new SugestaoResponseDTO(
				sugestao.getNomeSugestao(), 
				sugestao.getCorLetra(), 
				sugestao.getCorFundo());
	}

}
