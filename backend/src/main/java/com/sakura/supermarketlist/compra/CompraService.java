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
		
		Compra compra = conversaoDTOParaEntidade(request);
		
		Compra resposta = compraRepository.save(compra);
		
		inserirItensNaCompra(compra, request.listaItens());
		
		return conversaoEntidadeParaDTO(resposta);	
		
	}
	
	@Transactional
	public CompraResponseDTO unirCompra(Long ideCompra, CompraRequestDTO request) {

		Compra compra = compraRepository.findById(ideCompra)
				.orElseThrow(() -> new CompraInexistenteException(ideCompra));

		compra.setValorTotal(calcularValorTotal(request));

		Compra resposta = compraRepository.save(compra);

		inserirItensNaCompra(compra, request.listaItens());
		
		return conversaoEntidadeParaDTO(resposta);

	}
	
	private void inserirItensNaCompra(Compra compra, List<ItemCompraRequestDTO> listaItens) {
		
		List<Long> listaID = extrairListaDeId(listaItens);
		
		List<Item> itensParaInserir = itemRepository.findAllByIdeItemInAndIndAtivoTrue(listaID);
				
		validarListaDeItens(listaID, itensParaInserir);
		
		Map<Long, Item> mapaItens = itensParaInserir.stream()
		        .collect(Collectors.toMap(Item::getIdeItem, item -> item));
		
		List<ItemCompra> itensDaCompra = new ArrayList<ItemCompra>();

		for(ItemCompraRequestDTO objeto : listaItens) {
			
			ItemCompra itemCompra = new ItemCompra();
			Item item = mapaItens.get(objeto.ideItem());
			
			
			itemCompra.setCompra(compra);
			itemCompra.setMarca(objeto.marca());
			itemCompra.setPreco(objeto.valor());
			itemCompra.setItem(item);
			
			itensDaCompra.add(itemCompra);
			
		}
		
		itemCompraRepository.saveAll(itensDaCompra);
		
		
		
		
		
		
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
	
	private BigDecimal calcularValorTotal(CompraRequestDTO request) {
		
		BigDecimal valorTotalCompra = BigDecimal.ZERO;

		for (ItemCompraRequestDTO objeto : request.listaItens()) {

			valorTotalCompra.add(objeto.valor());
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
	
	private Compra conversaoDTOParaEntidade(CompraRequestDTO request) {
		
		Compra compra = new Compra();
		
		compra.setDataCompra(request.dataCompra());
		compra.setValorTotal(calcularValorTotal(request));
		
		return compra;
		
		
		

		

	}

}
