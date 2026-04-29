package com.sakura.supermarketlist.item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sakura.supermarketlist.categoria.Categoria;
import com.sakura.supermarketlist.categoria.CategoriaRepository;
import com.sakura.supermarketlist.categoria.dto.CategoriaResponseDTO;
import com.sakura.supermarketlist.categoria.exception.CategoriaInexistenteException;
import com.sakura.supermarketlist.common.dto.ExclusaoResponseDTO;
import com.sakura.supermarketlist.item.dto.AtualizacaoEstoqueRequestDTO;
import com.sakura.supermarketlist.item.dto.DashboardResponseDTO;
import com.sakura.supermarketlist.item.dto.ItemRequestDTO;
import com.sakura.supermarketlist.item.dto.ItemResponseDTO;
import com.sakura.supermarketlist.item.exception.ItemDuplicadoNaCategoria;
import com.sakura.supermarketlist.item.exception.ItemInexistenteException;
import com.sakura.supermarketlist.item.exception.ListaItemInexistenteException;
import com.sakura.supermarketlist.item.exception.NenhumaCategoriaCadastrada;

@Service
public class ItemService {
	@Autowired
	private ItemRepository repository;

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public ItemResponseDTO buscarItemPorId(Long ideItem) {
		Item item = repository.findByIdeItemAndIndAtivoTrue(ideItem)
				.orElseThrow(() -> new ItemInexistenteException(ideItem));

		return conversaoEntidadeParaDTO(item);
	}

	public List<ItemResponseDTO> buscarTodosItensAtivosOrdenadosPorCategoriaENome() {
		List<Item> lista = repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc();

		return listaDeResposta(lista);

	}

	public List<ItemResponseDTO> buscarTodosItensAtivosOrdenadosPorDataCriacao() {
		List<Item> lista = repository.findByIndAtivoTrueOrderByDtcCriacaoDesc();

		return listaDeResposta(lista);
	}

	public List<ItemResponseDTO> filtrarItensAtivosPorNome(String nome) {
		List<Item> lista = repository.findByNomeItemContainingIgnoreCaseAndIndAtivoTrueOrderByNomeItemAsc(nome);

		return listaDeResposta(lista);
	}

	public List<ItemResponseDTO> filtrarItensAtivosPorCategoria(Long ideCategoria) {
		List<Item> lista = repository.findByCategoriaIdeCategoriaAndIndAtivoTrueOrderByNomeItemAsc(ideCategoria);

		return listaDeResposta(lista);
	}

	public List<ItemResponseDTO> filtrarItensAtivosNacategoriaPorNome(Long ideCategoria, String nome) {
		List<Item> lista = repository
				.findByCategoriaIdeCategoriaAndNomeItemContainingIgnoreCaseAndIndAtivoTrueOrderByNomeItemAsc(
						ideCategoria, nome);

		return listaDeResposta(lista);
	}

	public ItemResponseDTO cadastrarItem(ItemRequestDTO request) {

		validarItem(request);
		validarInclusao(request);

		Item item = conversaoDTOParaEntidade(request);

		Item objeto = repository.save(item);

		return conversaoEntidadeParaDTO(objeto);

	}

	public ItemResponseDTO editarItem(ItemRequestDTO request, Long ideItem) {

		Item item = repository.findByIdeItemAndIndAtivoTrue(ideItem)
				.orElseThrow(() -> new ItemInexistenteException(ideItem));

		validarItem(request);
		validarEdicao(request, ideItem);

		atualizarDados(item, request);

		Item objeto = repository.save(item);

		return conversaoEntidadeParaDTO(objeto);

	}

	public ExclusaoResponseDTO excluirItem(Long ideItem) {

		Item item = repository.findByIdeItemAndIndAtivoTrue(ideItem)
				.orElseThrow(() -> new ItemInexistenteException(ideItem));

		LocalDateTime dataExclusao = LocalDateTime.now();
		List<Item> itensParaExclusao = new ArrayList<Item>();

		item.setIndAtivo(false);
		item.setDtcExclusao(dataExclusao);

		repository.save(item);
		itensParaExclusao.add(item);

		return objetoRespostaDeExclusao(1, dataExclusao, itensParaExclusao);
	}

	@Transactional
	public ExclusaoResponseDTO excluirItem(List<Long> listaItens) {

		validarListaDeExclusao(listaItens);

		LocalDateTime dataExclusao = LocalDateTime.now();

		List<Item> itensParaExclusao = repository.findAllByIdeItemInAndIndAtivoTrue(listaItens);

		for (Item item : itensParaExclusao) {
			item.setIndAtivo(false);
			item.setDtcExclusao(dataExclusao);
		}

		repository.saveAll(itensParaExclusao);

		return objetoRespostaDeExclusao(itensParaExclusao.size(), dataExclusao, itensParaExclusao);
	}
	
	@Transactional
	public List<ItemResponseDTO> atualizarEstoque(List<AtualizacaoEstoqueRequestDTO> listaRequest) {
		
		List<Long> todosIds = extrairListaDeId(listaRequest);
		
		List<Item> itensRecuperados = repository.findAllByIdeItemInAndIndAtivoTrue(todosIds);

		validarListaDeAtualizacao(todosIds, itensRecuperados);

		List<Item> itensAtualizados = new ArrayList<>();
		Map<Long, Item> mapaItens = itensRecuperados.stream()
		        .collect(Collectors.toMap(Item::getIdeItem, item -> item));

		for (AtualizacaoEstoqueRequestDTO request : listaRequest) {

			Item item = mapaItens.get(request.ideItem());

			if (item.getQuantidadeEstoque() != request.quantidadeNova()) {
				
				item.setQuantidadeEstoque(request.quantidadeNova());
				itensAtualizados.add(item);
			}

		}

		if (!itensAtualizados.isEmpty()) {
	        repository.saveAll(itensAtualizados);
	    }

		return listaDeResposta(itensAtualizados);

	}
	
	public DashboardResponseDTO capturarIndicadores() {
		Integer categorias = categoriaRepository.countByIndAtivoTrue();
		Integer itens = repository.countByIndAtivoTrue();
		Integer itensZerados = repository.countByQuantidadeEstoqueAndIndAtivoTrue(0);
		
		return new DashboardResponseDTO(categorias, itens, itensZerados);
		
	}

	private void validarItem(ItemRequestDTO request) {

		if (!categoriaRepository.existsByIndAtivoTrue()) {
			throw new NenhumaCategoriaCadastrada();
		}

		if (!categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(request.categoria())) {
			throw new CategoriaInexistenteException(request.categoria());
		}

	}

	private void validarInclusao(ItemRequestDTO request) {
		if (repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue(request.nome(), request.categoria())) {
			throw new ItemDuplicadoNaCategoria();
		}
	}

	private void validarEdicao(ItemRequestDTO request, Long ideItem) {
		if (repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrueAndIdeItemNot(request.nome(),
				request.categoria(), ideItem)) {
			throw new ItemDuplicadoNaCategoria();
		}
	}


	private void validarListaDeExclusao(List<Long> idesItem) {
		List<Item> itensParaExclusao = repository.findAllByIdeItemInAndIndAtivoTrue(idesItem);

		if (itensParaExclusao.size() != idesItem.size()) {
			Set<Long> idsEncontrados = itensParaExclusao.stream().map(Item::getIdeItem).collect(Collectors.toSet());

			List<Long> idsNaoEncontrados = idesItem.stream().filter(id -> !idsEncontrados.contains(id))
					.collect(Collectors.toList());

			throw new ListaItemInexistenteException(idsNaoEncontrados);
		}

	}

	private void validarListaDeAtualizacao(List<Long> idsRequest, List<Item> itensEncontrados) {

		   if (itensEncontrados.size() != idsRequest.size()) {
		        Set<Long> idsEncontrados = itensEncontrados.stream()
		            .map(Item::getIdeItem)
		            .collect(Collectors.toSet());
		        
		        List<Long> idsNaoEncontrados = idsRequest.stream()
		            .filter(id -> !idsEncontrados.contains(id))
		            .collect(Collectors.toList());
		        
		        if(!idsNaoEncontrados.isEmpty()) {
		        	throw new ListaItemInexistenteException(idsNaoEncontrados);
		        }
		        
		    }

	}

	private List<Long> extrairListaDeId(List<AtualizacaoEstoqueRequestDTO> listaRequest) {

		List<Long> listaID = new ArrayList<Long>();

		for (AtualizacaoEstoqueRequestDTO item : listaRequest) {

			listaID.add(item.ideItem());

		}

		return listaID;
	}

	private List<ItemResponseDTO> listaDeResposta(List<Item> lista) {

		List<ItemResponseDTO> listaFinal = new ArrayList<ItemResponseDTO>();

		for (Item item : lista) {
			ItemResponseDTO objeto = conversaoEntidadeParaDTO(item);

			listaFinal.add(objeto);
		}

		return listaFinal;
	}

	private Item conversaoDTOParaEntidade(ItemRequestDTO request) {
		Item item = new Item();
		Categoria categoria = categoriaRepository.findById(request.categoria())
				.orElseThrow(() -> new CategoriaInexistenteException(request.categoria()));

		item.setNomeItem(request.nome());
		item.setUnidadeMedida(request.unidadeMedida());
		item.setQuantidadeEstoque(request.quantidadeEstoque());
		item.setLimiteCompra(request.limiteCompra());
		item.setDataUltimaCompra(request.dataUltimaCompra());
		item.setCategoria(categoria);
		item.setDuracaoDias(request.duracaoDias());

		return item;

	}

	private ItemResponseDTO conversaoEntidadeParaDTO(Item item) {
		Categoria categoria = item.getCategoria();

		CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
				categoria.getIdeCategoria(),
				categoria.getDscCategoria(), 
				categoria.getCorLetra(), 
				categoria.getCorFundo(), 
				categoria.isIndAtivo());

		ItemResponseDTO objeto = new ItemResponseDTO(
				item.getIdeItem(), item.getNomeItem(), 
				item.getUnidadeMedida(),
				item.getQuantidadeEstoque(), 
				item.getLimiteCompra(), 
				item.getDataUltimaCompra(), 
				categoriaDTO,
				item.getDuracaoDias(), 
				item.isIndAtivo());

		return objeto;

	}
	

	private void atualizarDados(Item item, ItemRequestDTO request) {
		Categoria categoria = categoriaRepository.findById(request.categoria())
				.orElseThrow(() -> new CategoriaInexistenteException(request.categoria()));

		item.setNomeItem(request.nome());
		item.setUnidadeMedida(request.unidadeMedida());
		item.setQuantidadeEstoque(request.quantidadeEstoque());
		item.setLimiteCompra(request.limiteCompra());
		item.setDataUltimaCompra(request.dataUltimaCompra());
		item.setCategoria(categoria);
		item.setDuracaoDias(request.duracaoDias());

	}

	private ExclusaoResponseDTO objetoRespostaDeExclusao(Integer quantidadeExcluida, LocalDateTime dataExclusao,
			List<Item> itensExcluidos) {
		return new ExclusaoResponseDTO(
				quantidadeExcluida, 
				dataExclusao,
				itensExcluidos.stream().map(Item::getNomeItem).collect(Collectors.toList()));
	}

}
