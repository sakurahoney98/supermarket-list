package com.sakura.supermarketlist.relatorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakura.supermarketlist.compra.ItemCompraRepository;
import com.sakura.supermarketlist.relatorio.dto.RelatorioGastoConsultaDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioGastoItemResponseDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioGastoResponseDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioMensalResponseDTO;

@Service
public class RelatorioService {

	@Autowired
	ItemCompraRepository itemCompraRepository;

	public List<RelatorioMensalResponseDTO> relatorioItensCompradosNoMes(int ano, int mes) {

		return itemCompraRepository.buscarItensPorMesEAno(ano, mes);

	}

	public RelatorioGastoResponseDTO relatoriogastoItemPorPeriodo(Long ideItem, LocalDate inicio, LocalDate fim) {

		List<RelatorioGastoConsultaDTO> lista = itemCompraRepository.buscarGastoPorPeriodo(ideItem, inicio, fim);

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

	private List<RelatorioGastoItemResponseDTO> montarListaRelatorioGastoItem(List<RelatorioGastoConsultaDTO> lista) {
		List<RelatorioGastoItemResponseDTO> listaFinal = new ArrayList<RelatorioGastoItemResponseDTO>();

		for (RelatorioGastoConsultaDTO item : lista) {
			RelatorioGastoItemResponseDTO objeto = conversaoDTOConsultaParaDTOResposta(item);

			listaFinal.add(objeto);
		}

		return listaFinal;

	}

	private RelatorioGastoItemResponseDTO conversaoDTOConsultaParaDTOResposta(RelatorioGastoConsultaDTO consulta) {

		BigDecimal valorTotalPago = BigDecimal.ZERO;

		if (consulta.preco() != null) {
			valorTotalPago = consulta.preco().multiply(BigDecimal.valueOf(consulta.quantidade()));
		}

		return new RelatorioGastoItemResponseDTO(consulta.dataCompra(), consulta.marca(), valorTotalPago);

	}

}
