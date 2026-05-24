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

import com.sakura.supermarketlist.categoria.Categoria;
import com.sakura.supermarketlist.categoria.CategoriaRepository;
import com.sakura.supermarketlist.categoria.exception.CategoriaInexistenteException;
import com.sakura.supermarketlist.common.dto.ExclusaoResponseDTO;
import com.sakura.supermarketlist.item.dto.AtualizacaoEstoqueRequestDTO;
import com.sakura.supermarketlist.item.dto.DashboardResponseDTO;
import com.sakura.supermarketlist.item.dto.ItemRequestDTO;
import com.sakura.supermarketlist.item.dto.ItemResponseDTO;
import com.sakura.supermarketlist.item.exception.ItemDuplicadoNaCategoriaException;
import com.sakura.supermarketlist.item.exception.ItemInexistenteException;
import com.sakura.supermarketlist.item.exception.ListaItemInexistenteException;
import com.sakura.supermarketlist.item.exception.NenhumaCategoriaCadastradaException;

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
	private List<AtualizacaoEstoqueRequestDTO> listaRequest;

	@BeforeEach
	void setUp() {

		categoriaTest = new Categoria();
		categoriaTest.setIdeCategoria(2L);
		categoriaTest.setDscCategoria("Categoria Teste");
		categoriaTest.setCorLetra("#fff");
		categoriaTest.setCorFundo("#000");
		categoriaTest.setIndAtivo(true);

		itemCadastro = new Item();
		itemCadastro.setIdeItem(1L);
		itemCadastro.setNomeItem("Item Sucesso");
		itemCadastro.setUnidadeMedida("1kg");
		itemCadastro.setQuantidadeEstoque(1);
		itemCadastro.setLimiteCompra(4);
		itemCadastro.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		itemCadastro.setCategoria(categoriaTest);
		itemCadastro.setDuracaoDias(10);
		itemCadastro.setIndAtivo(true);

		itemEditado = new Item();
		itemEditado.setIdeItem(1L);
		itemEditado.setNomeItem("Item Editado");
		itemEditado.setUnidadeMedida("1kg");
		itemEditado.setQuantidadeEstoque(1);
		itemEditado.setLimiteCompra(4);
		itemEditado.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		itemEditado.setCategoria(categoriaTest);
		itemEditado.setDuracaoDias(10);
		itemEditado.setIndAtivo(true);

		requestCadastro = new ItemRequestDTO("Item Sucesso", "1Kg", 1, 4, LocalDate.of(2026, 3, 14), 2L, 10);
		requestEdicao = new ItemRequestDTO("Item Editado", "1Kg", 1, 4, LocalDate.of(2026, 3, 14), 2L, 10);

		Item item1 = new Item();
		item1.setIdeItem(1L);
		item1.setNomeItem("Item 1");
		item1.setUnidadeMedida("1kg");
		item1.setQuantidadeEstoque(1);
		item1.setLimiteCompra(1);
		item1.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item1.setCategoria(categoriaTest);
		item1.setDuracaoDias(30);
		item1.setIndAtivo(true);

		Item item2 = new Item();
		item2.setIdeItem(2L);
		item2.setNomeItem("Item 2");
		item2.setUnidadeMedida("1kg");
		item2.setQuantidadeEstoque(0);
		item2.setLimiteCompra(2);
		item2.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item2.setCategoria(categoriaTest);
		item2.setDuracaoDias(30);
		item2.setIndAtivo(true);

		Item item3 = new Item();
		item3.setIdeItem(3L);
		item3.setNomeItem("Item 3");
		item3.setUnidadeMedida("1kg");
		item3.setQuantidadeEstoque(1);
		item3.setLimiteCompra(2);
		item3.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item3.setCategoria(categoriaTest);
		item3.setDuracaoDias(10);
		item3.setIndAtivo(true);

		Item item4 = new Item();
		item4.setIdeItem(4L);
		item4.setNomeItem("Item 4");
		item4.setUnidadeMedida("1kg");
		item4.setQuantidadeEstoque(1);
		item4.setLimiteCompra(4);
		item4.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item4.setCategoria(categoriaTest);
		item4.setDuracaoDias(12);
		item4.setIndAtivo(true);

		listaItem = new ArrayList<>();
		listaItem.add(item1);
		listaItem.add(item2);
		listaItem.add(item3);
		listaItem.add(item4);

		listaIDs = new ArrayList<>();
		listaIDs.add(1L);
		listaIDs.add(2L);
		listaIDs.add(3L);
		listaIDs.add(4L);

		listaIDsVazia = new ArrayList<>();

		listaItensIncompleta = new ArrayList<>();
		listaItensIncompleta.add(listaItem.get(0)); 
		listaItensIncompleta.add(listaItem.get(2)); 

		listaRequest = new ArrayList<>();
	}

	// =========================================================
	// CADASTRAR
	// =========================================================

	@Test
	void cadastrarItemComSucesso() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(2L)).thenReturn(true);
		when(repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue("Item Sucesso", 2L)).thenReturn(false);
		when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoriaTest));
		when(repository.save(any(Item.class))).thenReturn(itemCadastro);

		ItemResponseDTO response = service.cadastrarItem(requestCadastro);

		assertNotNull(response);
		assertEquals("Item Sucesso", response.nome());
		assertEquals(2L, response.categoria().id());
		verify(repository, times(1)).save(any(Item.class));
	}

	@Test
	void cadastrarItemNenhumaCategoriaCadastrada() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(false);

		NenhumaCategoriaCadastradaException exception = assertThrows(NenhumaCategoriaCadastradaException.class,
				() -> service.cadastrarItem(requestCadastro));

		assertEquals("Necessário haver pelo menos uma categoria cadastrada.", exception.getMessage());
		verify(repository, never()).save(any(Item.class));
	}

	@Test
	void cadastrarItemCategoriaInexistente() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(2L)).thenReturn(false);

		CategoriaInexistenteException exception = assertThrows(CategoriaInexistenteException.class,
				() -> service.cadastrarItem(requestCadastro));

		assertEquals("Identificador 2 não existe ou a categoria já se encontra inativada.", exception.getMessage());
		verify(repository, never()).save(any(Item.class));
	}

	@Test
	void cadastrarItemDuplicadoNaCategoria() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(2L)).thenReturn(true);
		when(repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue("Item Sucesso", 2L)).thenReturn(true);

		ItemDuplicadoNaCategoriaException exception = assertThrows(ItemDuplicadoNaCategoriaException.class,
				() -> service.cadastrarItem(requestCadastro));

		assertEquals("Já existe um item com o mesmo nome cadastrado nessa categoria.", exception.getMessage());
		verify(repository, never()).save(any(Item.class));
	}

	// =========================================================
	// EDITAR
	// =========================================================

	@Test
	void editarItemComSucesso() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.of(itemCadastro));
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(2L)).thenReturn(true);
		when(repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrueAndIdeItemNot("Item Editado", 2L, 1L))
				.thenReturn(false);
		when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoriaTest));
		when(repository.save(any(Item.class))).thenReturn(itemEditado);

		ItemResponseDTO response = service.editarItem(requestEdicao, 1L);

		assertEquals("Item Editado", response.nome());
		verify(repository, times(1)).save(any(Item.class));
	}

	@Test
	void editarItemInexistente() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.empty());

		ItemInexistenteException exception = assertThrows(ItemInexistenteException.class,
				() -> service.editarItem(requestEdicao, 1L));

		assertEquals("Identificador 1 não existe ou o item já se encontra inativado.", exception.getMessage());
		verify(repository, never()).save(any(Item.class));
	}

	@Test
	void editarItemNenhumaCategoriaCadastrada() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.of(itemCadastro));
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(false);

		NenhumaCategoriaCadastradaException exception = assertThrows(NenhumaCategoriaCadastradaException.class,
				() -> service.editarItem(requestEdicao, 1L));

		assertEquals("Necessário haver pelo menos uma categoria cadastrada.", exception.getMessage());
		verify(repository, never()).save(any(Item.class));
	}

	@Test
	void editarItemCategoriaInexistente() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.of(itemCadastro));
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(2L)).thenReturn(false);

		CategoriaInexistenteException exception = assertThrows(CategoriaInexistenteException.class,
				() -> service.editarItem(requestEdicao, 1L));

		assertEquals("Identificador 2 não existe ou a categoria já se encontra inativada.", exception.getMessage());
		verify(repository, never()).save(any(Item.class));
	}

	@Test
	void editarItemDuplicadoNaCategoria() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.of(itemCadastro));
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(2L)).thenReturn(true);
		when(repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrueAndIdeItemNot("Item Editado", 2L, 1L))
				.thenReturn(true);

		ItemDuplicadoNaCategoriaException exception = assertThrows(ItemDuplicadoNaCategoriaException.class,
				() -> service.editarItem(requestEdicao, 1L));

		assertEquals("Já existe um item com o mesmo nome cadastrado nessa categoria.", exception.getMessage());
		verify(repository, never()).save(any(Item.class));
	}

	// =========================================================
	// EXCLUIR
	// =========================================================

	@Test
	void excluirItemComSucesso() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.of(itemCadastro));

		ExclusaoResponseDTO response = service.excluirItem(1L);

		assertEquals(1, response.quantidadeExcluida());
		assertFalse(itemCadastro.isIndAtivo());
		assertNotNull(itemCadastro.getDtcExclusao());
		verify(repository, times(1)).save(any(Item.class));
	}

	@Test
	void excluirItemInexistente() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.empty());

		ItemInexistenteException exception = assertThrows(ItemInexistenteException.class,
				() -> service.excluirItem(1L));

		assertEquals("Identificador 1 não existe ou o item já se encontra inativado.", exception.getMessage());
		verify(repository, never()).save(any(Item.class));
	}

	@Test
	void excluirListaItemComSucesso() {
		when(repository.findAllByIdeItemInAndIndAtivoTrue(listaIDs)).thenReturn(listaItem);

		ExclusaoResponseDTO response = service.excluirItem(listaIDs);

		assertEquals(4, response.quantidadeExcluida());
		assertFalse(listaItem.get(0).isIndAtivo());
		assertFalse(listaItem.get(1).isIndAtivo());
		assertFalse(listaItem.get(2).isIndAtivo());
		assertFalse(listaItem.get(3).isIndAtivo());
		verify(repository, times(1)).saveAll(any());
	}

	@Test
	void excluirListaVazia() {

		when(repository.findAllByIdeItemInAndIndAtivoTrue(listaIDsVazia)).thenReturn(List.of());

		ExclusaoResponseDTO response = service.excluirItem(listaIDsVazia);

		assertEquals(0, response.quantidadeExcluida());
		verify(repository, never()).save(any(Item.class));
	}

	@Test
	void excluirListaItemInexistente() {
		when(repository.findAllByIdeItemInAndIndAtivoTrue(listaIDs)).thenReturn(listaItensIncompleta);

		ListaItemInexistenteException exception = assertThrows(ListaItemInexistenteException.class,
				() -> service.excluirItem(listaIDs));

		assertEquals("Identificador [2, 4] não existe ou o item já se encontra inativado.", exception.getMessage());
		verify(repository, never()).saveAll(any());
	}

	// =========================================================
	// BUSCAR / FILTRAR
	// =========================================================

	@Test
	void buscarItemPorIdComSucesso() {
		when(repository.findByIdeItemAndIndAtivoTrue(1L)).thenReturn(Optional.of(listaItem.get(0)));

		ItemResponseDTO response = service.buscarItemPorId(1L);

		assertNotNull(response);
		assertEquals("Item 1", response.nome());
	}

	@Test
	void buscarItemPorIdInexistente() {
		when(repository.findByIdeItemAndIndAtivoTrue(99L)).thenReturn(Optional.empty());

		ItemInexistenteException exception = assertThrows(ItemInexistenteException.class,
				() -> service.buscarItemPorId(99L));

		assertEquals("Identificador 99 não existe ou o item já se encontra inativado.", exception.getMessage());
	}

	@Test
	void buscarTodosItensOrdenadosPorCategoriaENome() {
		when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc()).thenReturn(listaItem);

		List<ItemResponseDTO> response = service.buscarTodosItensAtivosOrdenadosPorCategoriaENome();

		assertNotNull(response);
		assertEquals(4, response.size());
		assertEquals("Item 1", response.get(0).nome());
	}

	@Test
	void buscarTodosItensOrdenadosPorNome() {
		when(repository.findByIndAtivoTrueOrderByNomeItemAsc()).thenReturn(listaItem);

		List<ItemResponseDTO> response = service.buscarTodosItensAtivosOrdenadosPorNome();

		assertNotNull(response);
		assertEquals(4, response.size());
	}

	@Test
	void buscarTodosItensOrdenadosPorData() {
		when(repository.findByIndAtivoTrueOrderByDtcCriacaoDesc()).thenReturn(listaItem);

		List<ItemResponseDTO> response = service.buscarTodosItensAtivosOrdenadosPorDataCriacao();

		assertNotNull(response);
		assertEquals(4, response.size());
	}

	@Test
	void filtrarItensPorNome() {
		when(repository.findByNomeItemContainingIgnoreCaseAndIndAtivoTrueOrderByNomeItemAsc("Item"))
				.thenReturn(listaItem);

		List<ItemResponseDTO> response = service.filtrarItensAtivosPorNome("Item");

		assertNotNull(response);
		assertEquals(4, response.size());
	}

	@Test
	void filtrarItensPorCategoria() {
		when(repository.findByCategoriaIdeCategoriaAndIndAtivoTrueOrderByNomeItemAsc(2L)).thenReturn(listaItem);

		List<ItemResponseDTO> response = service.filtrarItensAtivosPorCategoria(2L);

		assertNotNull(response);
		assertEquals(4, response.size());
	}

	@Test
	void filtrarItensPorCategoriaENome() {
		when(repository.findByCategoriaIdeCategoriaAndNomeItemContainingIgnoreCaseAndIndAtivoTrueOrderByNomeItemAsc(2L,
				"Item 1")).thenReturn(List.of(listaItem.get(0)));

		List<ItemResponseDTO> response = service.filtrarItensAtivosNacategoriaPorNome(2L, "Item 1");

		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals("Item 1", response.get(0).nome());
	}

	// =========================================================
	// ATUALIZAR ESTOQUE
	// =========================================================

	@Test
	void atualizarEstoqueTodosItensModificados() {

		listaRequest.add(new AtualizacaoEstoqueRequestDTO(1L, 1, 2));
		listaRequest.add(new AtualizacaoEstoqueRequestDTO(2L, 0, 2));
		listaRequest.add(new AtualizacaoEstoqueRequestDTO(3L, 1, 2));
		listaRequest.add(new AtualizacaoEstoqueRequestDTO(4L, 1, 2));

		when(repository.findAllByIdeItemInAndIndAtivoTrue(listaIDs)).thenReturn(listaItem);

		List<ItemResponseDTO> response = service.atualizarEstoque(listaRequest);

		assertEquals(4, response.size());
		assertEquals(2, response.get(0).quantidadeEstoque());
		assertEquals(2, response.get(1).quantidadeEstoque());
		assertEquals(2, response.get(2).quantidadeEstoque());
		assertEquals(2, response.get(3).quantidadeEstoque());
		verify(repository, times(1)).saveAll(any());
	}

	@Test
	void atualizarEstoqueNenhumItemModificado() {

		listaRequest.add(new AtualizacaoEstoqueRequestDTO(1L, 1, 1));
		listaRequest.add(new AtualizacaoEstoqueRequestDTO(2L, 0, 0));
		listaRequest.add(new AtualizacaoEstoqueRequestDTO(3L, 1, 1));
		listaRequest.add(new AtualizacaoEstoqueRequestDTO(4L, 1, 1));

		when(repository.findAllByIdeItemInAndIndAtivoTrue(listaIDs)).thenReturn(listaItem);

		List<ItemResponseDTO> response = service.atualizarEstoque(listaRequest);

		assertTrue(response.isEmpty());
		verify(repository, never()).saveAll(any());
	}

	@Test
	void atualizarEstoqueListaVazia() {
		List<ItemResponseDTO> response = service.atualizarEstoque(listaRequest);

		assertTrue(response.isEmpty());
		verify(repository, never()).saveAll(any());
	}

	@Test
	void atualizarEstoqueItemInexistente() {
		List<Long> idsComInexistente = List.of(1L, 99L);
		listaRequest.add(new AtualizacaoEstoqueRequestDTO(1L, 1, 2));
		listaRequest.add(new AtualizacaoEstoqueRequestDTO(99L, 1, 2));

		when(repository.findAllByIdeItemInAndIndAtivoTrue(idsComInexistente)).thenReturn(List.of(listaItem.get(0)));

		ListaItemInexistenteException exception = assertThrows(ListaItemInexistenteException.class,
				() -> service.atualizarEstoque(listaRequest));

		assertEquals("Identificador [99] não existe ou o item já se encontra inativado.", exception.getMessage());
		verify(repository, never()).saveAll(any());
	}

	// =========================================================
	// DASHBOARD
	// =========================================================

	@Test
	void capturarIndicadoresDashboard() {
		when(categoriaRepository.countByIndAtivoTrue()).thenReturn(3);
		when(repository.countByIndAtivoTrue()).thenReturn(4);
		when(repository.countByQuantidadeEstoqueAndIndAtivoTrue(0)).thenReturn(1);

		DashboardResponseDTO response = service.capturarIndicadores();

		assertNotNull(response);
		assertEquals(3, response.categorias());
		assertEquals(4, response.itens());
		assertEquals(1, response.itensZerados());
	}
}