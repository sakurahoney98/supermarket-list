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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
		List<String> itensComVinculo = new ArrayList<String>();
		
		itensComVinculo.add("Item teste");
		
		when(itemRepository.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(itensComVinculo);
		when(repository.findByIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(Optional.of(categoria));
		
		CategoriaVinculadaItensAtivosException exception = assertThrows(CategoriaVinculadaItensAtivosException.class, () -> {
			service.excluirCategoria(1L);
		});
		
		assertEquals(itensComVinculo.toString(), exception.getMessage());
		
		verify(repository, never()).save(any(Categoria.class));
		
	}
	
	@Test
	void excluirCategoriaSemVinculo() {
		
		List<String> itensComVinculo = new ArrayList<String>();

		
		when(itemRepository.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(itensComVinculo);
		when(repository.findByIdeCategoriaAndIndAtivoTrue(1L)).thenReturn(Optional.of(categoria));
		
		service.excluirCategoria(1L);
		
		verify(repository,times(1)).save(any(Categoria.class));
		
		assertFalse(categoria.isIndAtivo());
		
		
	}

}
