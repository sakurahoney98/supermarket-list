package com.sakura.supermarketlist.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakura.supermarketlist.categoria.Categoria;
import com.sakura.supermarketlist.categoria.CategoriaRepository;
import com.sakura.supermarketlist.categoria.exception.CategoriaInexistenteException;
import com.sakura.supermarketlist.item.exception.ItemDuplicadoNaCategoria;
import com.sakura.supermarketlist.item.exception.NenhumaCategoriaCadastrada;

@Service
public class ItemService {
	@Autowired
	private ItemRepository repository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public ItemResponseDTO cadastrarItem(ItemRequestDTO request) {
		
			validarCadastro(request);
		
			Item item = conversaoDTOParaEntidade(request);

			Item objeto = repository.save(item);

			return conversaoEntidadeParaDTO(objeto);
		

	}

	private void validarCadastro(ItemRequestDTO request) {
		
		if (!categoriaRepository.existsByIndAtivoTrue()) {
			throw new NenhumaCategoriaCadastrada();
		}
		
		if (!categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(request.categoria())) {
			throw new CategoriaInexistenteException();
		}
		
		if (repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue(request.nome(),
				request.categoria())) {
			throw new ItemDuplicadoNaCategoria();
		}
		
		
	}

	private Item conversaoDTOParaEntidade(ItemRequestDTO request) {
		Item item = new Item();
		Categoria categoria = categoriaRepository.findById(request.categoria())
				.orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

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
		ItemResponseDTO objeto = new ItemResponseDTO(item.getIdeItem(), 
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

}
