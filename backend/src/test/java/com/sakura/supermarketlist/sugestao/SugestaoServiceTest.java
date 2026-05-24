package com.sakura.supermarketlist.sugestao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SugestaoServiceTest {

    @Mock
    private SugestaoRepository repository;

    @InjectMocks
    private SugestaoService service;

    private List<Sugestao> sugestoesMock;

    @BeforeEach
    void setUp() {
        Sugestao s1 = new Sugestao();
        s1.setIdeSugestao(1L);
        s1.setNomeSugestao("Arroz");
        s1.setCorLetra("#166534");
        s1.setCorFundo("#dcfce7");
        s1.setIndAtivo(true);

        Sugestao s2 = new Sugestao();
        s2.setIdeSugestao(2L);
        s2.setNomeSugestao("Feijão");
        s2.setCorLetra("#7f1d1d");
        s2.setCorFundo("#fee2e2");
        s2.setIndAtivo(true);

        sugestoesMock = List.of(s1, s2);
    }

    @Test
    void buscarSugestoesComResultado() {
        when(repository.findByIndAtivoTrue()).thenReturn(sugestoesMock);

        List<SugestaoResponseDTO> response = service.buscarSugestoesAtivas();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals("Arroz",   response.get(0).nome());
        assertEquals("#166534", response.get(0).corLetra());
        assertEquals("#dcfce7", response.get(0).corFundo());

        assertEquals("Feijão",  response.get(1).nome());
        assertEquals("#7f1d1d", response.get(1).corLetra());
        assertEquals("#fee2e2", response.get(1).corFundo());

        verify(repository, times(1)).findByIndAtivoTrue();
    }

    @Test
    void buscarSugestoesSemResultado() {
        when(repository.findByIndAtivoTrue()).thenReturn(List.of());

        List<SugestaoResponseDTO> response = service.buscarSugestoesAtivas();

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(repository, times(1)).findByIndAtivoTrue();
    }
}