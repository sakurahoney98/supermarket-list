package com.sakura.supermarketlist.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import com.sakura.supermarketlist.categoria.Categoria;
import com.sakura.supermarketlist.categoria.CategoriaRepository;
import com.sakura.supermarketlist.categoria.exception.CategoriaInexistenteException;
import com.sakura.supermarketlist.common.dto.ExclusaoResponseDTO;
import com.sakura.supermarketlist.item.dto.ItemRequestDTO;
import com.sakura.supermarketlist.item.dto.ItemResponseDTO;
import com.sakura.supermarketlist.item.exception.ItemDuplicadoNaCategoria;
import com.sakura.supermarketlist.item.exception.ItemInexistenteException;
import com.sakura.supermarketlist.item.exception.ListaItemInexistenteException;
import com.sakura.supermarketlist.item.exception.NenhumaCategoriaCadastrada;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
	
	@Mock
	private ItemRepository repository;
	@Mock
	private CategoriaRepository categoriaRepository;

	@InjectMocks
	private ItemService service;
	
	private Item itemCadastro;
	private Item itemEditado;
	private ItemRequestDTO requestCadastro;
	private ItemRequestDTO requestEdicao;
	private Categoria categoriaTest;
	private List<Long> listaIDs;
	private List<Long> listaIDsVazia;
	private List<Item> listaItem;
	private List<Item> listaItensIncompleta;
	
	@BeforeEach
	void setUp() {
		
		categoriaTest = new Categoria(2L);
		
		itemCadastro = new Item();
		itemCadastro.setNomeItem("Item Sucesso");
		itemCadastro.setUnidadeMedida("1kg");
		itemCadastro.setQuantidadeEstoque(1);
		itemCadastro.setLimiteCompra(4);
		itemCadastro.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		itemCadastro.setCategoria(categoriaTest);
		itemCadastro.setDuracaoDias(10);
		
		itemEditado = new Item();
		BeanUtils.copyProperties(itemCadastro, itemEditado);
		itemEditado.setNomeItem("Item Editado");

		requestCadastro = new ItemRequestDTO("Item Sucesso", "1Kg", 1, 4, LocalDate.of(2026,3,14), 2L, 10);
		requestEdicao = new ItemRequestDTO("Item Editado", "1Kg", 1, 4, LocalDate.of(2026,3,14), 2L, 10);
		
		Item item1 = new Item();
		item1.setIdeItem(1L);
		item1.setNomeItem("Item 1");
		item1.setUnidadeMedida("1kg");
		item1.setQuantidadeEstoque(1);
		item1.setLimiteCompra(4);
		item1.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item1.setCategoria(categoriaTest);
		item1.setDuracaoDias(10);
		
		Item item2 = new Item();
		item2.setIdeItem(2L);
		item2.setNomeItem("Item 2");
		item2.setUnidadeMedida("1kg");
		item2.setQuantidadeEstoque(1);
		item2.setLimiteCompra(4);
		item2.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item2.setCategoria(categoriaTest);
		item2.setDuracaoDias(10);
		
		Item item3 = new Item();
		item3.setIdeItem(3L);
		item3.setNomeItem("Item 3");
		item3.setUnidadeMedida("1kg");
		item3.setQuantidadeEstoque(1);
		item3.setLimiteCompra(4);
		item3.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item3.setCategoria(categoriaTest);
		item3.setDuracaoDias(10);
		
		listaItem = new ArrayList<Item>();
		
		listaItem.add(item1);
		listaItem.add(item2);
		listaItem.add(item3);
		
		listaIDs = new ArrayList<Long>();
		
		listaIDs.add(listaItem.get(0).getIdeItem());
		listaIDs.add(listaItem.get(1).getIdeItem());
		listaIDs.add(listaItem.get(2).getIdeItem());
		
		listaIDsVazia = new ArrayList<Long>();
		
		listaItensIncompleta = new ArrayList<Item>();
		
		listaItensIncompleta.add(listaItem.get(0));
		listaItensIncompleta.add(listaItem.get(2));
		
		
		
		
		
	}
	
	@Test
	void cadastrarItemComSucesso() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(requestCadastro.categoria())).thenReturn(true);
		when(repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue(requestCadastro.nome(), requestCadastro.categoria())).thenReturn(false);
		when(categoriaRepository.findById(requestCadastro.categoria())).thenReturn(Optional.of(categoriaTest));
		when(repository.save(any(Item.class))).thenReturn(itemCadastro);
		
		ItemResponseDTO response = service.cadastrarItem(requestCadastro);
		
		assertNotNull(response);
		assertEquals("Item Sucesso", response.nome());
		
		repository.deleteById(response.id());
		
		
	}
	
	@Test
	void cadastrarItemNenhumaCategoriaCadastrada() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(false);
	
		
		NenhumaCategoriaCadastrada exception = assertThrows(NenhumaCategoriaCadastrada.class, () -> {
			service.cadastrarItem(requestCadastro);
		});

		assertEquals("Necessário haver pelo menos uma categoria cadastrada.", exception.getMessage());
		
		verify(repository, never()).save(any(Item.class));
		
		
	}
	
	@Test
	void cadastrarItemCategoriaInexistente() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(requestCadastro.categoria())).thenReturn(false);
	
		
		CategoriaInexistenteException exception = assertThrows(CategoriaInexistenteException.class, () -> {
			service.cadastrarItem(requestCadastro);
		});

		assertEquals("Identificador 2 não existe ou a categoria já se encontra inativada.", exception.getMessage());
		
		verify(repository, never()).save(any(Item.class));
		
		
	}
	
	@Test
	void cadastrarItemDuplicadoNaCategoria() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(requestCadastro.categoria())).thenReturn(true);
		when(repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue(requestCadastro.nome(), requestCadastro.categoria())).thenReturn(true);
		
		
		ItemDuplicadoNaCategoria exception = assertThrows(ItemDuplicadoNaCategoria.class, () -> {
			service.cadastrarItem(requestCadastro);
		});
		

		assertEquals("Já existe um item com o mesmo nome cadastrado nessa categoria.", exception.getMessage());
		
		verify(repository, never()).save(any(Item.class));
		
		
	}
	
	@Test
	void editarItemSucesso() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(requestEdicao.categoria())).thenReturn(true);
		when(categoriaRepository.findById(requestCadastro.categoria())).thenReturn(Optional.of(categoriaTest));
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.of(itemCadastro));
		when(repository.save(itemCadastro)).thenReturn(itemEditado);
		
		ItemResponseDTO response = service.editarItem(requestEdicao, 1L);
		
		assertEquals("Item Editado", response.nome());

		verify(repository, times(1)).save(any(Item.class));
		
	}
	
	@Test
	void editarItemInexistente() {
		
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.empty());
		
		ItemInexistenteException exception = assertThrows(ItemInexistenteException.class, () -> {
			service.editarItem(requestEdicao, 1L);
		});
		
		
		
		assertEquals("Identificador 1 não existe ou o item já se encontra inativado.", exception.getMessage());

		verify(repository, never()).save(any(Item.class));
		
	}
	
	@Test
	void excluirItemComSucesso() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.of(itemCadastro));
		
		ExclusaoResponseDTO response = service.excluirItem(1L);
		
		assertEquals(1, response.quantidadeExcluida());
		assertFalse(itemCadastro.isIndAtivo());
		
		verify(repository, times(1)).save(any(Item.class));
		
	}
	
	@Test
	void excluirItemInexistente() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.empty());
		
		ItemInexistenteException exception = assertThrows(ItemInexistenteException.class, () -> {
			service.excluirItem(1L);
		});
		
		assertEquals("Identificador 1 não existe ou o item já se encontra inativado.", exception.getMessage());
		
		verify(repository, never()).save(any(Item.class));
		
	}
	
	@Test
	void excluirListaItemComSucesso() {

		when(repository.findAllByIdeItemInAndIndAtivoTrue(listaIDs)).thenReturn(listaItem);
		
		ExclusaoResponseDTO response = service.excluirItem(listaIDs);
		
		assertEquals(3, response.quantidadeExcluida());
		assertFalse(listaItem.get(0).isIndAtivo());
		assertFalse(listaItem.get(1).isIndAtivo());
		assertFalse(listaItem.get(2).isIndAtivo());
		
		verify(repository, times(1)).saveAll(any(ArrayList.class));
		
	}
	
	@Test
	void excluirListaVazia() {
		
		ExclusaoResponseDTO response = service.excluirItem(listaIDsVazia);
		
		assertEquals(0, response.quantidadeExcluida());
		
		verify(repository, never()).save(any(Item.class));
		
	}
	
	
	@Test
	void excluirListaItemInexistente() {
		
		when(repository.findAllByIdeItemInAndIndAtivoTrue(listaIDs)).thenReturn(listaItensIncompleta);
		
		
		ListaItemInexistenteException exception = assertThrows(ListaItemInexistenteException.class, () -> {
			service.excluirItem(listaIDs);
		});
		
		
		assertEquals("Identificador [2] não existe ou o item já se encontra inativado.", exception.getMessage());
		assertTrue(listaItem.get(0).isIndAtivo());
		assertTrue(listaItem.get(2).isIndAtivo());
		
		verify(repository, never()).save(any(Item.class));
		
	}
	
	@Test
	void exibirTodosOsItens() {
		when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc()).thenReturn(listaItem);
		
		List<ItemResponseDTO> response = service.buscarTodosItensAtivosOrdenadosPorCategoriaENome();
		
		assertEquals(3, response.size());
		
	}
	
	@Test
	void exibirDetalhesDoItem() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.of(listaItem.get(0)));
		
		ItemResponseDTO response = service.buscarItemPorId(1L);
		
		assertEquals(listaItem.get(0).getNomeItem(), response.nome());
		
	}
	
	



}
