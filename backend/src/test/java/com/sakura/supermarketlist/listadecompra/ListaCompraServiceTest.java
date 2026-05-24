package com.sakura.supermarketlist.listadecompra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import com.sakura.supermarketlist.listacompra.ListaCompraService;
import com.sakura.supermarketlist.listacompra.dto.ListaCompraResponseDTO;

@ExtendWith(MockitoExtension.class)
public class ListaCompraServiceTest {

    @Mock
    private ItemRepository repository;

    @InjectMocks
    private ListaCompraService service;

    private Categoria categoriaTest;

    @BeforeEach
    void setUp() {
        categoriaTest = new Categoria();
        categoriaTest.setIdeCategoria(2L);
        categoriaTest.setDscCategoria("Categoria Teste");
        categoriaTest.setCorLetra("#FFFFFF");
        categoriaTest.setCorFundo("#000000");
        categoriaTest.setIndAtivo(true);
    }

 
    private Item criarItem(Long id, String nome, int estoque, int limite, int duracao) {
        Item item = new Item();
        item.setIdeItem(id);
        item.setNomeItem(nome);
        item.setUnidadeMedida("1kg");
        item.setQuantidadeEstoque(estoque);
        item.setLimiteCompra(limite);
        item.setDuracaoDias(duracao);
        item.setDataUltimaCompra(LocalDate.of(2026, 3, 14));
        item.setCategoria(categoriaTest);
        return item;
    }

    // =========================================================
    // estoque >= limiteCompra → sugerido = 0
    // =========================================================
    @Test
    void itemComEstoqueNoLimite() {
        // estoque(1) >= limite(1) → sugerido = 0
        Item item = criarItem(1L, "Item 1", 1, 1, 30);
        when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc())
                .thenReturn(List.of(item));

        List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(0, response.get(0).quantidadeSugerida());
    }

    // =========================================================
    // duração > 29, estoque == 0 → sugerido = limiteCompra
    // =========================================================
    @Test
    void itemComEstoqueZeroEDuracaoLonga() {
        // duração(30) > 29, estoque(0) == 0 → sugerido = limiteCompra(2)
        Item item = criarItem(2L, "Item 2", 0, 2, 30);
        when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc())
                .thenReturn(List.of(item));

        List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(2, response.get(0).quantidadeSugerida());
    }

    // =========================================================
    // duração > 29, estoque > 0 → sugerido = 0
    // =========================================================
    @Test
    void itemComEstoquePositivoEDuracaoLonga() {
        // duração(35) > 29, estoque(1) > 0 → sugerido = 0
        Item item = criarItem(3L, "Item 3", 1, 4, 35);
        when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc())
                .thenReturn(List.of(item));

        List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(0, response.get(0).quantidadeSugerida());
    }

    // =========================================================
    // duração <= 29, estoque == 0 → sugerido = limiteCompra
    // =========================================================
    @Test
    void itemComEstoqueZeroEDuracaoCurta() {
        // duração(10) <= 29, estoque(0) == 0 → sugerido = limiteCompra(2)
        Item item = criarItem(4L, "Item 4", 0, 2, 10);
        when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc())
                .thenReturn(List.of(item));

        List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(2, response.get(0).quantidadeSugerida());
    }

    // =========================================================
    // duração <= 29, estoque > 0
    // quantidadeBase(1) <= quantidadePorDuracao(2) → usa quantidadeBase
    // =========================================================
    @Test
    void itemComQuantidadeBaseMenorOuIgualDuracao() {
        // duração(10), estoque(1), limite(2)
        // base = 2-1 = 1
        // fator = ceil(30/10) = 3 → porDuracao = 3-1 = 2
        // base(1) > porDuracao(2)? NÃO → sugerido = base = 1
        Item item = criarItem(5L, "Item 5", 1, 2, 10);
        when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc())
                .thenReturn(List.of(item));

        List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1, response.get(0).quantidadeSugerida());
    }

    // =========================================================
    // duração <= 29, estoque > 0
    // quantidadeBase(3) > quantidadePorDuracao(2) → usa quantidadePorDuracao
    // =========================================================
    @Test
    void itemComQuantidadeBaseMaiorQuePorDuracao() {
        // duração(12), estoque(1), limite(4)
        // base = 4-1 = 3
        // fator = ceil(30/12) = 3 → porDuracao = 3-1 = 2
        // base(3) > porDuracao(2)? SIM → sugerido = porDuracao = 2
        Item item = criarItem(6L, "Item 6", 1, 4, 12);
        when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc())
                .thenReturn(List.of(item));

        List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(2, response.get(0).quantidadeSugerida());
    }

    // =========================================================
    // duração <= 29, estoque > 0
    // quantidadePorDuracao negativa → usa quantidadeBase
    // =========================================================
    @Test
    void itemComQuantidadePorDuracaoNegativa() {
        // duração(20), estoque(3), limite(6)
        // base = 6-3 = 3
        // fator = ceil(30/20) = 2 → porDuracao = 2-3 = -1
        // porDuracao(-1) > 0? NÃO → sugerido = base = 3
        Item item = criarItem(7L, "Item 7", 3, 6, 20);
        when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc())
                .thenReturn(List.of(item));

        List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(3, response.get(0).quantidadeSugerida());
    }

    // =========================================================
    // VALIDAÇÃO
    // =========================================================
    @Test
    void retornaListaVazia() {
        when(repository.findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc())
                .thenReturn(List.of());

        List<ListaCompraResponseDTO> response = service.gerarListaDeCompras();

        assertNotNull(response);
        assertEquals(0, response.size());
    }
}