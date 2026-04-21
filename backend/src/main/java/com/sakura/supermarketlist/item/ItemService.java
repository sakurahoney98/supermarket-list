package com.sakura.supermarketlist.item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakura.supermarketlist.categoria.Categoria;
import com.sakura.supermarketlist.categoria.CategoriaRepository;
import com.sakura.supermarketlist.categoria.exception.CategoriaInexistenteException;
import com.sakura.supermarketlist.common.dto.ExclusaoResponseDTO;
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

	public ItemResponseDTO cadastrarItem(ItemRequestDTO request) {

		validarItem(request);

		Item item = conversaoDTOParaEntidade(request);

		Item objeto = repository.save(item);

		return conversaoEntidadeParaDTO(objeto);

	}

	public ItemResponseDTO editarItem(ItemRequestDTO request, Long ideItem) {

		Item item = repository.findByIdeItemAndIndAtivoTrue(ideItem).orElseThrow(() -> new ItemInexistenteException(ideItem));

		validarItem(request);

		atualizarDados(item, request);

		Item objeto = repository.save(item);

		return conversaoEntidadeParaDTO(objeto);

	}
	
	public ExclusaoResponseDTO excluirItem(Long ideItem) {
		
		Item item = repository.findByIdeItemAndIndAtivoTrue(ideItem).orElseThrow(() -> new ItemInexistenteException(ideItem));
		
		LocalDateTime dataExclusao = LocalDateTime.now();
		List <Item> itensParaExclusao = new ArrayList<Item>();
		
		item.setIndAtivo(false);
		item.setDtcExclusao(dataExclusao);
		
		repository.save(item);
		itensParaExclusao.add(item);
		
		return objetoRespostaDeExclusao(1, dataExclusao, itensParaExclusao);
	}
	
	public ExclusaoResponseDTO excluirItem(List<Long> listaItens) {
		
		validarListaDeExclusao(listaItens);
		
		LocalDateTime dataExclusao = LocalDateTime.now();
		List <Item> itensParaExclusao = new ArrayList<Item>();
		
		for(Long ideItem : listaItens) {
		Item item = repository.findByIdeItemAndIndAtivoTrue(ideItem).orElseThrow(() -> new ItemInexistenteException(ideItem));
		
		item.setIndAtivo(false);
		item.setDtcExclusao(dataExclusao);
		
		repository.save(item);
		itensParaExclusao.add(item);
		}
		
		return objetoRespostaDeExclusao(itensParaExclusao.size(), dataExclusao, itensParaExclusao);
	}

	private void validarItem(ItemRequestDTO request) {

		if (!categoriaRepository.existsByIndAtivoTrue()) {
			throw new NenhumaCategoriaCadastrada();
		}

		if (!categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(request.categoria())) {
			throw new CategoriaInexistenteException(request.categoria());
		}

		if (repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue(request.nome(), request.categoria())) {
			throw new ItemDuplicadoNaCategoria();
		}

	}
	
	private void validarListaDeExclusao(List<Long> idesItem) {
		List<Item> itensParaExclusao = repository.findAllByIdeItemInAndIndAtivoTrue(idesItem);

		if (itensParaExclusao.size() != idesItem.size()) {
			Set<Long> idsEncontrados = itensParaExclusao.stream().map(Item::getIdeItem)
					.collect(Collectors.toSet());

			List<Long> idsNaoEncontrados = idesItem.stream().filter(id -> !idsEncontrados.contains(id))
					.collect(Collectors.toList());

			throw new ListaItemInexistenteException(idsNaoEncontrados);
		}

		
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
		ItemResponseDTO objeto = new ItemResponseDTO(
				item.getIdeItem(), 
				item.getNomeItem(), 
				item.getUnidadeMedida(),
				item.getQuantidadeEstoque(), 
				item.getLimiteCompra(), 
				item.getDataUltimaCompra(), 
				item.getCategoria(),
				item.getDuracaoDias(), 
				item.isIndAtivo());

		return objeto;

	}

	private void atualizarDados(Item item, ItemRequestDTO request) {
		Categoria categoria = categoriaRepository.findById(request.categoria())
				.orElseThrow(() -> new CategoriaInexistenteException(request.categoria()));

		item.setNomeItem(request.nome());
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
