package com.sakura.supermarketlist.relatorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakura.supermarketlist.compra.CompraRepository;
import com.sakura.supermarketlist.compra.ItemCompraRepository;
import com.sakura.supermarketlist.relatorio.dto.IntervaloAnosCompraDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioConsultaDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioGastoResponseDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioMensalResponseDTO;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private ItemCompraRepository itemCompraRepository;

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private RelatorioService service;

    private LocalDate inicio;
    private LocalDate fim;

    @BeforeEach
    void setUp() {
        inicio = LocalDate.of(2024, 1, 1);
        fim    = LocalDate.of(2024, 12, 31);
    }

    // =========================================================
    // BUSCA / FILTRO
    // =========================================================

    @Test
    void intervaloAnos() {
        IntervaloAnosCompraDTO dto = new IntervaloAnosCompraDTO(2023, 2025);
        when(compraRepository.buscarIntervaloAnos()).thenReturn(dto);

        IntervaloAnosCompraDTO response = service.intervaloAnosCompra();

        assertNotNull(response);
        assertEquals(2023, response.anoInicio());
        assertEquals(2025, response.anoFim());
        verify(compraRepository, times(1)).buscarIntervaloAnos();
    }

    // =========================================================
    // RECUPERAÇÃO DE DADOS
    // =========================================================

    @Test
    void relatorioMensalComItens() {
        List<RelatorioMensalResponseDTO> mock = List.of(
            new RelatorioMensalResponseDTO("Arroz", "Tio João", new BigDecimal("5.99"), 2),
            new RelatorioMensalResponseDTO("Feijão", "Camil", new BigDecimal("8.50"), 1)
        );
        when(itemCompraRepository.buscarItensPorMesEAno(2024, 3)).thenReturn(mock);

        List<RelatorioMensalResponseDTO> response = service.relatorioItensCompradosNoMes(2024, 3);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Macarrão", response.get(0).nomeItem());
        assertEquals("Feijão", response.get(1).nomeItem());
        verify(itemCompraRepository, times(1)).buscarItensPorMesEAno(2024, 3);
    }

    @Test
    void relatorioMensalSemItens() {
        when(itemCompraRepository.buscarItensPorMesEAno(2024, 3)).thenReturn(List.of());

        List<RelatorioMensalResponseDTO> response = service.relatorioItensCompradosNoMes(2024, 3);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }


    @Test
    void relatorioGastoComCompras() {

        List<RelatorioConsultaDTO> mock = List.of(
            new RelatorioConsultaDTO(LocalDate.of(2024, 3, 1),  "Marca A", new BigDecimal("10.00"), 2),
            new RelatorioConsultaDTO(LocalDate.of(2024, 6, 15), "Marca B", new BigDecimal("5.00"),  3),
            new RelatorioConsultaDTO(LocalDate.of(2024, 1, 20), "Marca C", new BigDecimal("8.00"),  1)
        );
        when(itemCompraRepository.buscarGastoPorPeriodo(1L, inicio, fim)).thenReturn(mock);

        RelatorioGastoResponseDTO response = service.relatorioGastoItemPorPeriodo(1L, inicio, fim);

        assertNotNull(response);

        assertEquals(new BigDecimal("43.00"), response.gastoTotal());
        assertEquals(3, response.historico().size());
      
        assertEquals(LocalDate.of(2024, 6, 15), response.historico().get(0).dataCompra());
        assertEquals(LocalDate.of(2024, 3, 1),  response.historico().get(1).dataCompra());
        assertEquals(LocalDate.of(2024, 1, 20), response.historico().get(2).dataCompra());

        verify(itemCompraRepository, times(1)).buscarGastoPorPeriodo(1L, inicio, fim);
    }

    @Test
    void relatorioGastoComPrecoNulo() {

        List<RelatorioConsultaDTO> mock = List.of(
            new RelatorioConsultaDTO(LocalDate.of(2024, 5, 10), "Marca X", null, 3)
        );
        when(itemCompraRepository.buscarGastoPorPeriodo(2L, inicio, fim)).thenReturn(mock);

        RelatorioGastoResponseDTO response = service.relatorioGastoItemPorPeriodo(2L, inicio, fim);

        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.gastoTotal());
        assertEquals(1, response.historico().size());
        assertEquals(BigDecimal.ZERO, response.historico().get(0).valorTotalPago());
    }

    @Test
    void relatorioGastoSemCompras() {
        when(itemCompraRepository.buscarGastoPorPeriodo(3L, inicio, fim)).thenReturn(List.of());

        RelatorioGastoResponseDTO response = service.relatorioGastoItemPorPeriodo(3L, inicio, fim);

        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.gastoTotal());
        assertTrue(response.historico().isEmpty());
    }
}