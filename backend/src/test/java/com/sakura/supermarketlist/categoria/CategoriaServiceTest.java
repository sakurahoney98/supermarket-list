package com.sakura.supermarketlist.categoria;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakura.supermarketlist.categoria.dto.CategoriaRequestDTO;
import com.sakura.supermarketlist.categoria.dto.CategoriaResponseDTO;
import com.sakura.supermarketlist.categoria.exception.CategoriaDuplicadaException;
import com.sakura.supermarketlist.categoria.exception.CategoriaVinculadaItensAtivosException;
import com.sakura.supermarketlist.common.dto.ExclusaoResponseDTO;
import com.sakura.supermarketlist.item.ItemRepository;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {
	@Mock
	private CategoriaRepository repository;
	@Mock
	private ItemRepository itemRepository;

	@InjectMocks
	private CategoriaService service;

	private CategoriaRequestDTO request;
	private Categoria categoria;
	private List<Categoria> categoriasMock;
	private List<Long> listaCategorias;
	private List<String> itensComVinculo;
	private List<String> itensComVinculoVazia;

	@BeforeEach
	void setUp() {
		// Dados para os testes
		request = new CategoriaRequestDTO("Teste", "#166534", "#dcfce7");

		categoria = new Categoria();
		categoria.setIdeCategoria(1L);
		categoria.setDscCategoria("Teste");
		categoria.setCorLetra("#FFFFFF");
		categoria.setCorFundo("#0000FF");
		categoria.setIndAtivo(true);

		categoriasMock = new ArrayList<>();

		Categoria cat1 = new Categoria();
		cat1.setIdeCategoria(1L);
		cat1.setDscCategoria("Zebra");
		cat1.setCorLetra("#fff");
		cat1.setCorFundo("#000");
		cat1.setIndAtivo(true);
		cat1.setDtcCriacao(LocalDateTime.of(2024, 1, 15, 10, 30));

		Categoria cat2 = new Categoria();
		cat2.setIdeCategoria(2L);
		cat2.setDscCategoria("Arroz");
		cat2.setCorLetra("#fff");
		cat2.setCorFundo("#000");
		cat2.setIndAtivo(true);
		cat2.setDtcCriacao(LocalDateTime.of(2024, 1, 10, 10, 30));

		Categoria cat3 = new Categoria();
		cat3.setIdeCategoria(3L);
		cat3.setDscCategoria("Bolo");
		cat3.setCorLetra("#fff");
		cat3.setCorFundo("#000");
		cat3.setIndAtivo(true);
		cat3.setDtcCriacao(LocalDateTime.of(2024, 1, 20, 10, 30));

		categoriasMock.add(cat1);
		categoriasMock.add(cat2);
		categoriasMock.add(cat3);
		
		listaCategorias = new ArrayList<Long>();
		listaCategorias.add(cat1.getIdeCategoria());
		listaCategorias.add(cat2.getIdeCategoria());
		listaCategorias.add(cat3.getIdeCategoria());
		
		itensComVinculo = new ArrayList<String>();
		itensComVinculo.add("Item teste");
		
		itensComVinculoVazia = new ArrayList<String>();

		
		

	}

	@Test
	void cadastrarCategoriaInexistente() {

		when(repository.existsByDscCategoriaAndIndAtivoTrue("Teste")).thenReturn(false);

		when(repository.save(any(Categoria.class))).thenReturn(categoria);

		CategoriaResponseDTO response = service.cadastrarCategoria(request);

		assertNotNull(response);
		assertEquals("Teste", response.nome());
		assertEquals("#FFFFFF", response.corLetra());
		assertEquals("#0000FF", response.corFundo());
		assertTrue(response.ativo());

		repository.deleteById(response.id());

	}

	@Test
	void cadastrarCategoriaExistente() {

		when(repository.existsByDscCategoriaAndIndAtivoTrue("Teste")).thenReturn(true);

		CategoriaDuplicadaException exception = assertThrows(CategoriaDuplicadaException.class, () -> {
			service.cadastrarCategoria(request);
		});

		assertEquals("Já existe uma categoria ativa com o nome Teste", exception.getMessage());

		verify(repository, never()).save(any(Categoria.class));

	}

	@Test
	void excluirCategoriaComVinculo() {

		when(itemRepository.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(itensComVinculo);
		when(repository.findByIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(Optional.of(categoria));

		CategoriaVinculadaItensAtivosException exception = assertThrows(CategoriaVinculadaItensAtivosException.class,
				() -> {
					service.excluirCategoria(1L);
				});

		assertEquals(itensComVinculo.toString(), exception.getMessage());

		verify(repository, never()).save(any(Categoria.class));

	}

	@Test
	void excluirCategoriaSemVinculo() {

		when(itemRepository.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(itensComVinculoVazia);
		when(repository.findByIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(Optional.of(categoria));

		service.excluirCategoria(1L);

		verify(repository, times(1)).save(any(Categoria.class));

		assertFalse(categoria.isIndAtivo());

	}
	
	@Test
	void excluirListaCategoriaComVinculo() {
		
		List<String> categoriasComBloqueioDeExclusao = new ArrayList<String>();
		categoriasComBloqueioDeExclusao.add(categoriasMock.get(0).getDscCategoria() + ": " + itensComVinculo.toString());

		when(itemRepository.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(itensComVinculo);
		when(repository.findAllByIdeCategoriaInAndIndAtivoTrue(listaCategorias)).thenReturn(categoriasMock);

		CategoriaVinculadaItensAtivosException exception = assertThrows(CategoriaVinculadaItensAtivosException.class,
				() -> {
					service.excluirCategoria(listaCategorias);
				});
		

		assertEquals(categoriasComBloqueioDeExclusao.toString(), exception.getMessage());

		verify(repository, never()).save(any(Categoria.class));
		

	}
	
	@Test
	void excluirListaCategoriaVazia() {

		when(itemRepository.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(itensComVinculoVazia);
		when(repository.findByIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(Optional.of(categoriasMock.get(0)));
		when(repository.findByIdeCategoriaAndIndAtivoTrue(2L)).thenReturn(Optional.of(categoriasMock.get(1)));
		when(repository.findByIdeCategoriaAndIndAtivoTrue(3L)).thenReturn(Optional.of(categoriasMock.get(2)));

		service.excluirCategoria(1L);
		service.excluirCategoria(2L);
		service.excluirCategoria(3L);

		verify(repository, times(3)).save(any(Categoria.class));

		assertFalse(categoriasMock.get(0).isIndAtivo());
		assertFalse(categoriasMock.get(1).isIndAtivo());
		assertFalse(categoriasMock.get(2).isIndAtivo());

	}
	
	@Test
	void excluirListaCategoriaSemVinculo() {

		when(itemRepository.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(itensComVinculoVazia);
		when(repository.findAllByIdeCategoriaInAndIndAtivoTrue(listaCategorias)).thenReturn(categoriasMock);

		ExclusaoResponseDTO response = service.excluirCategoria(listaCategorias);
		
		assertEquals(3, response.quantidadeExcluida());

		assertFalse(categoriasMock.get(0).isIndAtivo());
		assertFalse(categoriasMock.get(1).isIndAtivo());
		assertFalse(categoriasMock.get(2).isIndAtivo());
		
		verify(repository, times(1)).saveAll(any(ArrayList.class));

	}

	@Test
	void capturarTodasCategoriasPorNome() {
		when(repository.findByIndAtivoTrueOrderByDscCategoriaAsc()).thenReturn(categoriasMock);
		
		service.buscarTodasCategoriasAtivasOrdenadasPorNome();

		verify(repository, times(1)).findByIndAtivoTrueOrderByDscCategoriaAsc();
	}
	
	@Test
	void capturarTodasCategoriasPorData() {
		when(repository.findByIndAtivoTrueOrderByDtcCriacaoDesc()).thenReturn(categoriasMock);
		
		service.buscarTodasCategoriasAtivasOrdenadasPorDataCriacao();

		verify(repository, times(1)).findByIndAtivoTrueOrderByDtcCriacaoDesc();
	}
	
	@Test
	void filtrarCategoriasPordata() {
		when(repository.findByDscCategoriaContainingIgnoreCaseAndIndAtivoTrueOrderByDscCategoriaAsc("Teste")).thenReturn(categoriasMock);
		
		service.filtrarCategoriasPorNome("Teste");

		verify(repository, times(1)).findByDscCategoriaContainingIgnoreCaseAndIndAtivoTrueOrderByDscCategoriaAsc("Teste");
	}

}
