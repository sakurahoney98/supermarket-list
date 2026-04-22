package com.sakura.supermarketlist.listadecompra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakura.supermarketlist.categoria.Categoria;
import com.sakura.supermarketlist.item.Item;
import com.sakura.supermarketlist.item.ItemRepository;
import com.sakura.supermarketlist.listacompra.ListaCompraResponseDTO;
import com.sakura.supermarketlist.listacompra.ListaCompraService;

@ExtendWith(MockitoExtension.class)
public class ListaCompraServiceTest {
	@Mock
	private ItemRepository repository;
	
	@InjectMocks
	private ListaCompraService service;
	
	private List<Item> listaItem;
	private Categoria categoriaTest;
	
	@BeforeEach
	void setUp() {
		
		categoriaTest = new Categoria(2L);
		
		Item item1 = new Item();
		item1.setIdeItem(1L);
		item1.setNomeItem("Item 1");
		item1.setUnidadeMedida("1kg");
		item1.setQuantidadeEstoque(1);
		item1.setLimiteCompra(1);
		item1.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item1.setCategoria(categoriaTest);
		item1.setDuracaoDias(30);
		
		Item item2 = new Item();
		item2.setIdeItem(2L);
		item2.setNomeItem("Item 2");
		item2.setUnidadeMedida("1kg");
		item2.setQuantidadeEstoque(0);
		item2.setLimiteCompra(2);
		item2.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item2.setCategoria(categoriaTest);
		item2.setDuracaoDias(30);
		
		Item item3 = new Item();
		item3.setIdeItem(3L);
		item3.setNomeItem("Item 3");
		item3.setUnidadeMedida("1kg");
		item3.setQuantidadeEstoque(1);
		item3.setLimiteCompra(2);
		item3.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item3.setCategoria(categoriaTest);
		item3.setDuracaoDias(10);
		
		Item item4 = new Item();
		item4.setIdeItem(1L);
		item4.setNomeItem("Item 1");
		item4.setUnidadeMedida("1kg");
		item4.setQuantidadeEstoque(1);
		item4.setLimiteCompra(4);
		item4.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item4.setCategoria(categoriaTest);
		item4.setDuracaoDias(12);
		
		Item item5 = new Item();
		item5.setIdeItem(1L);
		item5.setNomeItem("Item 5");
		item5.setUnidadeMedida("1kg");
		item5.setQuantidadeEstoque(3);
		item5.setLimiteCompra(6);
		item5.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
		item5.setCategoria(categoriaTest);
		item5.setDuracaoDias(20);
		
		listaItem = new ArrayList<Item>();
		
		listaItem.add(item1);
		listaItem.add(item2);
		listaItem.add(item3);
		listaItem.add(item4);
		listaItem.add(item5);
		
		
		
	}
	
	@Test
	void gerarListaDeCompras() {
		when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc()).thenReturn(listaItem);
		
		List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();
		
		assertEquals(0, response.get(0).quantidadeSugerida());
		assertEquals(2, response.get(1).quantidadeSugerida());
		assertEquals(1, response.get(2).quantidadeSugerida());
		assertEquals(2, response.get(3).quantidadeSugerida());
		assertEquals(3, response.get(4).quantidadeSugerida());
	}

}
