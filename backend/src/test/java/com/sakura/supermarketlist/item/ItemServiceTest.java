package com.sakura.supermarketlist.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

import com.sakura.supermarketlist.categoria.Categoria;
import com.sakura.supermarketlist.categoria.CategoriaRepository;
import com.sakura.supermarketlist.categoria.exception.CategoriaInexistenteException;
import com.sakura.supermarketlist.item.exception.ItemDuplicadoNaCategoria;
import com.sakura.supermarketlist.item.exception.NenhumaCategoriaCadastrada;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
	
	@Mock
	private ItemRepository repository;
	@Mock
	private CategoriaRepository categoriaRepository;

	@InjectMocks
	private ItemService service;
	
	private Item item;
	private ItemRequestDTO requestSucesso;
	private Categoria categoriaTest;
	
	@BeforeEach
	void setUp() {
		
		categoriaTest = new Categoria(2L);
		
		item = new Item();
		item.setNomeItem("Item Sucesso");
		item.setUnidadeMedida("1kg");
		item.setQuantidadeEstoque(1);
		item.setLimiteCompra(4);
		item.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item.setCategoria(categoriaTest);
		item.setDuracaoDias(10);

		requestSucesso = new ItemRequestDTO("Item Sucesso", "1Kg", 1, 4, LocalDate.of(2026,3,14), 2L, 10);
		
	}
	
	@Test
	void cadastrarItemComSucesso() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(requestSucesso.categoria())).thenReturn(true);
		when(repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue(requestSucesso.nome(), requestSucesso.categoria())).thenReturn(false);
		when(categoriaRepository.findById(requestSucesso.categoria())).thenReturn(Optional.of(categoriaTest));
		when(repository.save(any(Item.class))).thenReturn(item);
		
		ItemResponseDTO response = service.cadastrarItem(requestSucesso);
		
		assertNotNull(response);
		assertEquals("Item Sucesso", response.nome());
		
		repository.deleteById(response.id());
		
		
	}
	
	@Test
	void cadastrarItemNenhumaCategoriaCadastrada() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(false);
	
		
		NenhumaCategoriaCadastrada exception = assertThrows(NenhumaCategoriaCadastrada.class, () -> {
			service.cadastrarItem(requestSucesso);
		});

		assertEquals("Necessário haver pelo menos uma categoria cadastrada.", exception.getMessage());
		
		verify(repository, never()).save(any(Item.class));
		
		
	}
	
	@Test
	void cadastrarItemCategoriaInexistente() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(requestSucesso.categoria())).thenReturn(false);
	
		
		CategoriaInexistenteException exception = assertThrows(CategoriaInexistenteException.class, () -> {
			service.cadastrarItem(requestSucesso);
		});

		assertEquals("Identificador não existe ou a categoria já se encontra inativada.", exception.getMessage());
		
		verify(repository, never()).save(any(Item.class));
		
		
	}
	
	@Test
	void cadastrarItemDuplicadoNaCategoria() {
		when(categoriaRepository.existsByIndAtivoTrue()).thenReturn(true);
		when(categoriaRepository.existsByIdeCategoriaAndIndAtivoTrue(requestSucesso.categoria())).thenReturn(true);
		when(repository.existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue(requestSucesso.nome(), requestSucesso.categoria())).thenReturn(true);
		
		
	
		
		ItemDuplicadoNaCategoria exception = assertThrows(ItemDuplicadoNaCategoria.class, () -> {
			service.cadastrarItem(requestSucesso);
		});

		assertEquals("Já existe um item com o mesmo nome cadastrado nessa categoria.", exception.getMessage());
		
		verify(repository, never()).save(any(Item.class));
		
		
	}



}
