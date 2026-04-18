package com.sakura.supermarketlist.categoria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakura.supermarketlist.categoria.exception.CategoriaDuplicadaException;
import com.sakura.supermarketlist.categoria.exception.CategoriaVinculadaItensAtivosException;
import com.sakura.supermarketlist.item.ItemRepository;

@Service
public class CategoriaService {
	@Autowired
	CategoriaRepository repository;

	@Autowired
	ItemRepository itemRepository;

	public CategoriaResponseDTO cadastrarCategoria(CategoriaRequestDTO request) {

		if (repository.existsByDscCategoriaAndIndAtivoTrue(request.nome())) {
			throw new CategoriaDuplicadaException(request.nome());

		}

		Categoria categoria = new Categoria();
		categoria.setDscCategoria(request.nome());
		categoria.setCorLetra(request.corLetra());
		categoria.setCorFundo(request.corFundo());

		Categoria objeto = repository.save(categoria);

		return conversaoEntidadeParaDTO(objeto);

	}

	public boolean excluirCategoria(Long ideCategoria) {
		Categoria categoria = repository.findByIdeCategoriaAndIndAtivoTrue(ideCategoria).orElseThrow(
				() -> new RuntimeException("Identificador não existe ou a categoria já se encontra inativada"));

		List<String> itensVinculadosCategoria = itemRepository
				.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(ideCategoria);

		if (isExclusaoPermitida(itensVinculadosCategoria)) {

			categoria.setIndAtivo(false);
			categoria.setDtcExclusao(LocalDateTime.now());

			repository.save(categoria);

		} else {
			throw new CategoriaVinculadaItensAtivosException(itensVinculadosCategoria);
		}

		return true;

	}

	public List<CategoriaResponseDTO> buscarTodasCategoriasAtivasOrdenadasPorNome() {
		List<Categoria> lista = repository.findByIndAtivoTrueOrderByDscCategoriaAsc();

		return geracaoDeListaDTO(lista);

	}

	public List<CategoriaResponseDTO> buscarTodasCategoriasAtivasOrdenadasPorDataCriacao() {
		List<Categoria> lista = repository.findByIndAtivoTrueOrderByDtcCriacaoDesc();

		return geracaoDeListaDTO(lista);

	}

	public List<CategoriaResponseDTO> filtrarCategoriasPorNome(String nome) {
		List<Categoria> lista = repository
				.findByDscCategoriaContainingIgnoreCaseAndIndAtivoTrueOrderByDscCategoriaAsc(nome);

		return geracaoDeListaDTO(lista);

	}

	private CategoriaResponseDTO conversaoEntidadeParaDTO(Categoria objeto) {
		CategoriaResponseDTO dto = new CategoriaResponseDTO(objeto.getIdeCategoria(), objeto.getDscCategoria(),
				objeto.getCorLetra(), objeto.getCorFundo(), objeto.isIndAtivo());
		
		return dto;
	}

	private List<CategoriaResponseDTO> geracaoDeListaDTO(List<Categoria> lista) {
		List<CategoriaResponseDTO> listaFinal = new ArrayList<CategoriaResponseDTO>();

		for (Categoria objeto : lista) {

			listaFinal.add(conversaoEntidadeParaDTO(objeto));
		}

		return listaFinal;

	}

	private boolean isExclusaoPermitida(List<String> itensVinculadosCategoria) {

		return itensVinculadosCategoria.isEmpty();

	}

}
