package com.sakura.supermarketlist.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sakura.supermarketlist.compra.dto.CompraRequestDTO;
import com.sakura.supermarketlist.compra.dto.CompraResponseDTO;
import com.sakura.supermarketlist.compra.dto.ItemCompraRequestDTO;
import com.sakura.supermarketlist.compra.exception.CompraInexistenteException;
import com.sakura.supermarketlist.compra.exception.ListaItensCompradosVaziaException;
import com.sakura.supermarketlist.item.Item;
import com.sakura.supermarketlist.item.ItemRepository;
import com.sakura.supermarketlist.item.exception.ListaItemInexistenteException;

@Service
public class CompraService {

	@Autowired
	CompraRepository compraRepository;

	@Autowired
	ItemCompraRepository itemCompraRepository;
	
	@Autowired
	ItemRepository itemRepository;

	public List<CompraResponseDTO> checarDataCompra(LocalDate data) {

		List<CompraResponseDTO> listaDatasIguais = new ArrayList<CompraResponseDTO>();

		if (compraRepository.existsByDataCompra(data)) {

			listaDatasIguais = montarListaResposta(compraRepository.findByDataCompra(data));
		}

		return listaDatasIguais;

	}
	
	@Transactional
	public CompraResponseDTO inserirNovaCompra (CompraRequestDTO request) {
		
		List<ItemCompraRequestDTO> listaFiltrada = request.listaItens().stream().filter(item -> item.quantidadeComprada() > 0)
				.toList();
		
		if(!listaFiltrada.isEmpty()) {
			
			Compra compra = conversaoDTOParaEntidade(request, listaFiltrada);
			
			Compra resposta = compraRepository.save(compra);
			
			inserirItensNaCompra(compra, listaFiltrada);
			
			return conversaoEntidadeParaDTO(resposta);	
			
		}
		
		throw new ListaItensCompradosVaziaException();
		
		
	}
	
	@Transactional
	public CompraResponseDTO unirCompra(Long ideCompra, CompraRequestDTO request) {
		
		List<ItemCompraRequestDTO> listaFiltrada = request.listaItens().stream().filter(item -> item.quantidadeComprada() > 0)
				.toList();
		
		if(!listaFiltrada.isEmpty()) {
			
			Compra compra = compraRepository.findById(ideCompra)
					.orElseThrow(() -> new CompraInexistenteException(ideCompra));
			
			BigDecimal novoValorTotal = compra.getValorTotal();
			novoValorTotal = novoValorTotal.add(calcularValorTotal(listaFiltrada));

			compra.setValorTotal(novoValorTotal);

			Compra resposta = compraRepository.save(compra);

			inserirItensNaCompra(compra, listaFiltrada);
			
			return conversaoEntidadeParaDTO(resposta);
			
		}
		
		throw new ListaItensCompradosVaziaException();

		

	}
	
	private void inserirItensNaCompra(Compra compra, List<ItemCompraRequestDTO> listaItens) {
		
		List<Long> listaID = extrairListaDeId(listaItens);
		
		List<Item> itensParaInserir = itemRepository.findAllByIdeItemInAndIndAtivoTrue(listaID);
				
		validarListaDeItens(listaID, itensParaInserir);
		
		Map<Long, Item> mapaItens = itensParaInserir.stream()
		        .collect(Collectors.toMap(Item::getIdeItem, item -> item));
		
		List<ItemCompra> itensDaCompra = new ArrayList<ItemCompra>();
		List<Item> itensParaAtualizar = new ArrayList<Item>();

		for(ItemCompraRequestDTO objeto : listaItens) {
			
			Item item = mapaItens.get(objeto.ideItem());
			ItemCompra itemCompra = itemCompraRepository.findByCompraIdeCompraAndPrecoAndMarca(compra.getIdeCompra(), objeto.valor(), objeto.marca());
			
			if(itemCompra != null) {
				
				int quantidadeNovaItemCompra = itemCompra.getQuantidade() + objeto.quantidadeComprada();
				itemCompra.setQuantidade(quantidadeNovaItemCompra);
				
			}else {
				
				itemCompra = new ItemCompra();
				
				itemCompra.setCompra(compra);
				itemCompra.setMarca(objeto.marca());
				itemCompra.setPreco(objeto.valor());
				itemCompra.setItem(item);
				itemCompra.setQuantidade(objeto.quantidadeComprada());
				
			}
			
			int quantidadeNovaItem = item.getQuantidadeEstoque() + objeto.quantidadeComprada();
			item.setQuantidadeEstoque(quantidadeNovaItem);
			item.setDataUltimaCompra(compra.getDataCompra());
			
			itensParaAtualizar.add(item);
			itensDaCompra.add(itemCompra);
			
		}
		
		itemCompraRepository.saveAll(itensDaCompra);
		itemRepository.saveAll(itensParaAtualizar);
			
	}
	
	private void validarListaDeItens(List<Long> listaID, List<Item> itensParaInserir) {

		if (itensParaInserir.size() != listaID.size()) {
			Set<Long> idsEncontrados = itensParaInserir.stream().map(Item::getIdeItem).collect(Collectors.toSet());

			List<Long> idsNaoEncontrados = listaID.stream().filter(id -> !idsEncontrados.contains(id))
					.collect(Collectors.toList());

			throw new ListaItemInexistenteException(idsNaoEncontrados);
		}
	}
	
	private List<Long> extrairListaDeId(List<ItemCompraRequestDTO> listaItens) {

		List<Long> listaID = new ArrayList<Long>();

		for (ItemCompraRequestDTO item : listaItens) {

			listaID.add(item.ideItem());

		}

		return listaID;
	}
	
	private BigDecimal calcularValorTotal(List<ItemCompraRequestDTO> lista) {
		
		BigDecimal valorTotalCompra = BigDecimal.ZERO;
		

		for (ItemCompraRequestDTO objeto : lista) {

			if (objeto.valor() != null) {
				
				BigDecimal valorTotalItem = objeto.valor();
				
				valorTotalItem = valorTotalItem.multiply(BigDecimal.valueOf(objeto.quantidadeComprada()));
				
	            valorTotalCompra = valorTotalCompra.add(valorTotalItem);
	        }
		}

		return valorTotalCompra;
	}


	private List<CompraResponseDTO> montarListaResposta(List<Compra> lista) {

		List<CompraResponseDTO> listaFinal = new ArrayList<CompraResponseDTO>();

		for (Compra compra : lista) {

			CompraResponseDTO objeto = conversaoEntidadeParaDTO(compra);

			listaFinal.add(objeto);

		}

		return listaFinal;

	}

	private CompraResponseDTO conversaoEntidadeParaDTO(Compra compra) {

		int quantidadeItensNaCompra = itemCompraRepository.countByCompraIdeCompra(compra.getIdeCompra());

		return new CompraResponseDTO(compra.getIdeCompra(), quantidadeItensNaCompra);

	}
	
	private Compra conversaoDTOParaEntidade(CompraRequestDTO request, List<ItemCompraRequestDTO> lista) {
		
		Compra compra = new Compra();
		
		compra.setDataCompra(request.dataCompra());
		compra.setValorTotal(calcularValorTotal(lista));
		
		return compra;
		
	}

}
