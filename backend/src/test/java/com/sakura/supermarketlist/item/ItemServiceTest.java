package com.sakura.supermarketlist.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import com.sakura.supermarketlist.item.exception.ItemDuplicadoNaCategoria;
import com.sakura.supermarketlist.item.exception.ItemInexistenteException;
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

		assertEquals("Identificador não existe ou a categoria já se encontra inativada.", exception.getMessage());
		
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
		when(repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue(requestEdicao.nome(), requestEdicao.categoria())).thenReturn(false);
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
		
		
		
		assertEquals("Identificador não existe ou o item já se encontra inativado.", exception.getMessage());

		verify(repository, never()).save(any(Item.class));
		
	}



}
