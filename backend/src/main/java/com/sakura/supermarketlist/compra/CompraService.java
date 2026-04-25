package com.sakura.supermarketlist.compra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraService {

	@Autowired
	CompraRepository compraRepository;

	@Autowired
	ItemCompraRepository itemCompraRepository;

	public List<CompraResponseDTO> checarDataCompra(LocalDate data) {

		List<CompraResponseDTO> listaDatasIguais = new ArrayList<CompraResponseDTO>();

		if (compraRepository.existsByDataCompra(data)) {

			listaDatasIguais = montarListaResposta(compraRepository.findByDataCompra(data));
		}

		return listaDatasIguais;

	}


	private List<CompraResponseDTO> montarListaResposta(List<Compra> lista) {

		List<CompraResponseDTO> listaFinal = new ArrayList<CompraResponseDTO>();

		for (Compra compra : lista) {

			CompraResponseDTO objeto = conversaoEntidadeParaDTO(compra);

			listaFinal.add(objeto);

		}

		return listaFinal;

	}

	private CompraResponseDTO conversaoEntidadeParaDTO(Compra compra) {

		int quantidadeItensNaCompra = itemCompraRepository.countByCompraIdeCompra(compra.getIdeCompra());

		return new CompraResponseDTO(compra.getIdeCompra(), quantidadeItensNaCompra);

	}

}
