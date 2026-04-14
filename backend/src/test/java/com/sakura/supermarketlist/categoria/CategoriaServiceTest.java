package com.sakura.supermarketlist.categoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {
	@Mock
	private CategoriaRepository repository;

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
	    
	    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
	        service.cadastrarCategoria(request);
	    });

	    assertEquals("Categoria já existe.", exception.getMessage());
	        

	}

}
