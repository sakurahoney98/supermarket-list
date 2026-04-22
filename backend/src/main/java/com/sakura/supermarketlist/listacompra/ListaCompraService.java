package com.sakura.supermarketlist.listacompra;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakura.supermarketlist.categoria.Categoria;
import com.sakura.supermarketlist.categoria.dto.CategoriaResponseDTO;
import com.sakura.supermarketlist.item.Item;
import com.sakura.supermarketlist.item.ItemRepository;

@Service
public class ListaCompraService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	public List<ListaCompraResponseDTO> gerarListaDeCompras() {
		List<Item> itensEstoque = itemRepository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc();
		
		List<ListaCompraResponseDTO> listaFinal = new ArrayList<ListaCompraResponseDTO>();
		
		for(Item item : itensEstoque) {
			
			if(item.getQuantidadeEstoque() >= item.getLimiteCompra()) {
				ListaCompraResponseDTO objeto = conversaoEntidadeParaDTO(item, 0);
				
				listaFinal.add(objeto);
			} else if (item.getDuracaoDias() > 29) {
				if(item.getQuantidadeEstoque() == 0) {
					ListaCompraResponseDTO objeto = conversaoEntidadeParaDTO(item, item.getLimiteCompra());
					
					listaFinal.add(objeto);
				}else {
					ListaCompraResponseDTO objeto = conversaoEntidadeParaDTO(item, 0);
					
					listaFinal.add(objeto);
				}
				
			}else {
				if(item.getQuantidadeEstoque() == 0) {
					ListaCompraResponseDTO objeto = conversaoEntidadeParaDTO(item, item.getLimiteCompra());
					
					listaFinal.add(objeto);
				}else {
					Integer quantidadeBase = item.getLimiteCompra() - item.getQuantidadeEstoque();

					Integer fatorDuracao = Math.ceilDiv(30, item.getDuracaoDias());
					Integer quantidadePorDuracao = fatorDuracao - item.getQuantidadeEstoque();
					
					Integer valorSugerido = quantidadePorDuracao > 0 && quantidadeBase > quantidadePorDuracao ? quantidadePorDuracao : quantidadeBase;
					
					ListaCompraResponseDTO objeto = conversaoEntidadeParaDTO(item, valorSugerido);
					
					listaFinal.add(objeto);
				}
			}
		}
		
		return listaFinal;
		
	}
	
	
	private ListaCompraResponseDTO conversaoEntidadeParaDTO(Item item, Integer quantidadeSugerida) {
		Categoria categoria = item.getCategoria();
		
		CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
				categoria.getIdeCategoria(),
				categoria.getDscCategoria(), 
				categoria.getCorLetra(), 
				categoria.getCorFundo(), 
				categoria.isIndAtivo());
		
		return new ListaCompraResponseDTO(
				item.getIdeItem(),
				item.getNomeItem(),
				item.getUnidadeMedida(),
				categoriaDTO,
				item.getQuantidadeEstoque(),
				quantidadeSugerida);
		
	}
	
	

}
