package com.sakura.supermarketlist.relatorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakura.supermarketlist.compra.CompraRepository;
import com.sakura.supermarketlist.compra.ItemCompraRepository;
import com.sakura.supermarketlist.relatorio.dto.IntervaloAnosCompraDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioConsultaDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioGastoItemResponseDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioGastoResponseDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioMensalResponseDTO;

@Service
public class RelatorioService {

	@Autowired
	ItemCompraRepository itemCompraRepository;
	
	@Autowired
	CompraRepository compraRepository;

	public IntervaloAnosCompraDTO intervaloAnosCompra() {
		
		return compraRepository.buscarIntervaloAnos();
	}
	
	public List<RelatorioMensalResponseDTO> relatorioItensCompradosNoMes(int ano, int mes) {

		return itemCompraRepository.buscarItensPorMesEAno(ano, mes);

	}

	public RelatorioGastoResponseDTO relatorioGastoItemPorPeriodo(Long ideItem, LocalDate inicio, LocalDate fim) {

		List<RelatorioConsultaDTO> lista = itemCompraRepository.buscarGastoPorPeriodo(ideItem, inicio, fim);
		lista = lista.stream().sorted(Comparator.comparing(RelatorioConsultaDTO::dataCompra).reversed())
			    .collect(Collectors.toList());

		List<RelatorioGastoItemResponseDTO> listaResposta = montarListaRelatorioGastoItem(lista);

		return new RelatorioGastoResponseDTO(calcularValorTotal(listaResposta), listaResposta);

	}
	

	private BigDecimal calcularValorTotal(List<RelatorioGastoItemResponseDTO> lista) {
		BigDecimal valorTotalCompra = BigDecimal.ZERO;

		for (RelatorioGastoItemResponseDTO objeto : lista) {

			valorTotalCompra = valorTotalCompra.add(objeto.valorTotalPago());
		}

		return valorTotalCompra;

	}

	private List<RelatorioGastoItemResponseDTO> montarListaRelatorioGastoItem(List<RelatorioConsultaDTO> lista) {
		List<RelatorioGastoItemResponseDTO> listaFinal = new ArrayList<RelatorioGastoItemResponseDTO>();

		for (RelatorioConsultaDTO item : lista) {
			RelatorioGastoItemResponseDTO objeto = conversaoDTOConsultaParaDTOResposta(item);

			listaFinal.add(objeto);
		}

		return listaFinal;

	}

	private RelatorioGastoItemResponseDTO conversaoDTOConsultaParaDTOResposta(RelatorioConsultaDTO consulta) {

		BigDecimal valorTotalPago = BigDecimal.ZERO;

		if (consulta.preco() != null) {
			valorTotalPago = consulta.preco().multiply(BigDecimal.valueOf(consulta.quantidade()));
		}

		return new RelatorioGastoItemResponseDTO(consulta.dataCompra(), consulta.marca(), valorTotalPago);

	}

}
