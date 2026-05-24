package com.sakura.supermarketlist.compra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakura.supermarketlist.compra.dto.CompraRequestDTO;
import com.sakura.supermarketlist.compra.dto.CompraResponseDTO;
import com.sakura.supermarketlist.compra.dto.ItemCompraRequestDTO;
import com.sakura.supermarketlist.compra.exception.CompraInexistenteException;
import com.sakura.supermarketlist.compra.exception.ListaItensCompradosVaziaException;
import com.sakura.supermarketlist.item.Item;
import com.sakura.supermarketlist.item.ItemRepository;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private ItemCompraRepository itemCompraRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CompraService service;

    private Item item1;
    private Item item2;
    private Compra compraMock;
    private ItemCompraRequestDTO itemRequest1;
    private ItemCompraRequestDTO itemRequest2;
    private ItemCompraRequestDTO itemRequestQuantidadeZero;

    @BeforeEach
    void setUp() {

        item1 = new Item();
        item1.setIdeItem(1L);
        item1.setNomeItem("Arroz");
        item1.setQuantidadeEstoque(5);
        item1.setDataUltimaCompra(LocalDate.of(2024, 1, 1));

        item2 = new Item();
        item2.setIdeItem(2L);
        item2.setNomeItem("Feijão");
        item2.setQuantidadeEstoque(3);
        item2.setDataUltimaCompra(LocalDate.of(2024, 1, 1));

        compraMock = new Compra();
        compraMock.setIdeCompra(10L);
        compraMock.setDataCompra(LocalDate.of(2024, 6, 15));
        compraMock.setValorTotal(new BigDecimal("50.00"));

        itemRequest1 = new ItemCompraRequestDTO(1L, 2, new BigDecimal("10.00"), "MarcaA");
        itemRequest2 = new ItemCompraRequestDTO(2L, 3, new BigDecimal("5.00"), "MarcaB");
        itemRequestQuantidadeZero = new ItemCompraRequestDTO(1L, 0, new BigDecimal("10.00"), "MarcaA");
    }

    // =========================================================
    // VALIDAÇÃO
    // =========================================================

    @Test
    void checarDataCompraComResultado() {
        LocalDate data = LocalDate.of(2024, 6, 15);

        when(compraRepository.existsByDataCompra(data)).thenReturn(true);
        when(compraRepository.findByDataCompraOrderByIdeCompra(data)).thenReturn(List.of(compraMock));
        when(itemCompraRepository.countByCompraIdeCompra(10L)).thenReturn(3);

        List<CompraResponseDTO> response = service.checarDataCompra(data);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(10L, response.get(0).ideCompra());
        assertEquals(3, response.get(0).quantidadeItens());
    }

    @Test
    void checarDataCompraSemResultado() {
        LocalDate data = LocalDate.of(2024, 6, 15);

        when(compraRepository.existsByDataCompra(data)).thenReturn(false);

        List<CompraResponseDTO> response = service.checarDataCompra(data);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(compraRepository, never()).findByDataCompraOrderByIdeCompra(any());
    }

    // =========================================================
    // CADASTRO
    // =========================================================

    @Test
    void inserirNovaCompra() {
        CompraRequestDTO request = new CompraRequestDTO(
                LocalDate.of(2024, 6, 20),
                List.of(itemRequest1, itemRequest2)
        );

  
        when(compraRepository.save(any(Compra.class))).thenAnswer(invocation -> {
            Compra c = invocation.getArgument(0);
            c.setIdeCompra(99L);
            return c;
        });

        when(itemRepository.findAllByIdeItemInAndIndAtivoTrue(List.of(1L, 2L)))
                .thenReturn(List.of(item1, item2));

   
        when(itemCompraRepository.findByCompraIdeCompraAndItemIdeItemAndPrecoAndMarca(
                eq(99L), eq(1L), eq(new BigDecimal("10.00")), eq("MarcaA"))).thenReturn(null);
        when(itemCompraRepository.findByCompraIdeCompraAndItemIdeItemAndPrecoAndMarca(
                eq(99L), eq(2L), eq(new BigDecimal("5.00")), eq("MarcaB"))).thenReturn(null);

        when(itemCompraRepository.countByCompraIdeCompra(99L)).thenReturn(2);

        CompraResponseDTO response = service.inserirNovaCompra(request);

        assertNotNull(response);
        assertEquals(99L, response.ideCompra());
        assertEquals(2, response.quantidadeItens());


        ArgumentCaptor<Compra> captor = ArgumentCaptor.forClass(Compra.class);
        verify(compraRepository).save(captor.capture());
        assertEquals(new BigDecimal("35.00"), captor.getValue().getValorTotal());

        
        assertEquals(7, item1.getQuantidadeEstoque());  
        assertEquals(6, item2.getQuantidadeEstoque());  

        verify(itemCompraRepository, times(1)).saveAll(anyList());
        verify(itemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void inserirNovaCompraItensZerados() {
        CompraRequestDTO request = new CompraRequestDTO(
                LocalDate.of(2024, 6, 20),
                List.of(itemRequestQuantidadeZero)
        );

        ListaItensCompradosVaziaException exception = assertThrows(
                ListaItensCompradosVaziaException.class,
                () -> service.inserirNovaCompra(request)
        );

        assertEquals("Nenhum item foi comprado.", exception.getMessage());
        verify(compraRepository, never()).save(any());
    }


    @Test
    void inserirNovaCompraItemDuplicado() {
        CompraRequestDTO request = new CompraRequestDTO(
                LocalDate.of(2024, 6, 20),
                List.of(itemRequest1)
        );

        when(compraRepository.save(any(Compra.class))).thenAnswer(invocation -> {
            Compra c = invocation.getArgument(0);
            c.setIdeCompra(99L);
            return c;
        });

        when(itemRepository.findAllByIdeItemInAndIndAtivoTrue(List.of(1L)))
                .thenReturn(List.of(item1));

        
        ItemCompra itemCompraExistente = new ItemCompra();
        itemCompraExistente.setIdeItemCompra(55L);
        itemCompraExistente.setQuantidade(3);
        itemCompraExistente.setItem(item1);

        when(itemCompraRepository.findByCompraIdeCompraAndItemIdeItemAndPrecoAndMarca(
                eq(99L), eq(1L), eq(new BigDecimal("10.00")), eq("MarcaA")))
                .thenReturn(itemCompraExistente);

        when(itemCompraRepository.countByCompraIdeCompra(99L)).thenReturn(1);

        service.inserirNovaCompra(request);

        assertEquals(5, itemCompraExistente.getQuantidade());
        verify(itemCompraRepository, times(1)).saveAll(anyList());
    }


    @Test
    void unirCompra() {
        CompraRequestDTO request = new CompraRequestDTO(
                LocalDate.of(2024, 6, 15),
                List.of(itemRequest1)
        );

        when(compraRepository.findById(10L)).thenReturn(Optional.of(compraMock));

        when(itemRepository.findAllByIdeItemInAndIndAtivoTrue(List.of(1L)))
                .thenReturn(List.of(item1));

        when(itemCompraRepository.findByCompraIdeCompraAndItemIdeItemAndPrecoAndMarca(
                eq(10L), eq(1L), eq(new BigDecimal("10.00")), eq("MarcaA"))).thenReturn(null);

        when(compraRepository.save(any(Compra.class))).thenReturn(compraMock);
        when(itemCompraRepository.countByCompraIdeCompra(10L)).thenReturn(4);

        CompraResponseDTO response = service.unirCompra(10L, request);

        assertNotNull(response);
        assertEquals(10L, response.ideCompra());
        assertEquals(4, response.quantidadeItens());

       
        assertEquals(new BigDecimal("70.00"), compraMock.getValorTotal());
    }

    @Test
    void unirCompraInexistente() {
        CompraRequestDTO request = new CompraRequestDTO(
                LocalDate.of(2024, 6, 15),
                List.of(itemRequest1)
        );

        when(compraRepository.findById(99L)).thenReturn(Optional.empty());

        CompraInexistenteException exception = assertThrows(
                CompraInexistenteException.class,
                () -> service.unirCompra(99L, request)
        );

        assertEquals("O identificador 99 não existe", exception.getMessage());
        verify(compraRepository, never()).save(any());
    }

    @Test
    void unirCompraItensZerados() {
        CompraRequestDTO request = new CompraRequestDTO(
                LocalDate.of(2024, 6, 15),
                List.of(itemRequestQuantidadeZero)
        );

        ListaItensCompradosVaziaException exception = assertThrows(
                ListaItensCompradosVaziaException.class,
                () -> service.unirCompra(10L, request)
        );

        assertEquals("Nenhum item foi comprado.", exception.getMessage());
        verify(compraRepository, never()).save(any());
        verify(compraRepository, never()).findById(any());
    }
}