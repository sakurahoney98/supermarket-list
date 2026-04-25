package com.sakura.supermarketlist.relatorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakura.supermarketlist.compra.ItemCompraRepository;

@Service
public class RelatorioService {

	@Autowired
	ItemCompraRepository itemCompraRepository;

	public List<RelatorioMensalResponseDTO> relatorioItensCompradosNoMes(int ano, int mes) {

		return itemCompraRepository.buscarItensPorMesEAno(ano, mes);

	}

	
}
