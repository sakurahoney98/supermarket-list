package com.sakura.supermarketlist.categoria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sakura.supermarketlist.categoria.exception.CategoriaDuplicadaException;
import com.sakura.supermarketlist.categoria.exception.CategoriaInexistenteException;
import com.sakura.supermarketlist.categoria.exception.CategoriaVinculadaItensAtivosException;
import com.sakura.supermarketlist.categoria.exception.ListaCategoriaInexistenteException;
import com.sakura.supermarketlist.common.dto.ExclusaoResponseDTO;
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

		Categoria categoria = conversaoDTOParaEntidade(request);

		Categoria objeto = repository.save(categoria);

		return conversaoEntidadeParaDTO(objeto);

	}

	public ExclusaoResponseDTO excluirCategoria(Long ideCategoria) {
		
		Categoria categoria = repository.findByIdeCategoriaAndIndAtivoTrue(ideCategoria)
				.orElseThrow(() -> new CategoriaInexistenteException(ideCategoria));

		validarItemDeExclusao(ideCategoria);

		LocalDateTime dataExclusao = LocalDateTime.now();
		List<Categoria> categoriasExcluidas = new ArrayList<Categoria>();

		dataExclusao = LocalDateTime.now();
		categoria.setIndAtivo(false);
		categoria.setDtcExclusao(dataExclusao);

		repository.save(categoria);
		
		categoriasExcluidas.add(categoria);

		return objetoRespostaDeExclusao(1, dataExclusao, categoriasExcluidas);

	}

	@Transactional
	public ExclusaoResponseDTO excluirCategoria(List<Long> listaCategorias) {
		List<Categoria> categoriasExcluidas = repository.findAllByIdeCategoriaInAndIndAtivoTrue(listaCategorias);

		validarListaDeExclusao(listaCategorias, categoriasExcluidas);

		LocalDateTime dataExclusao = LocalDateTime.now();
		
		for (Categoria categoria : categoriasExcluidas) {

			categoria.setIndAtivo(false);
			categoria.setDtcExclusao(dataExclusao);

		}
		
		repository.saveAll(categoriasExcluidas);

		return objetoRespostaDeExclusao(categoriasExcluidas.size(), dataExclusao, categoriasExcluidas);

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

	private Categoria conversaoDTOParaEntidade(CategoriaRequestDTO request) {
		Categoria categoria = new Categoria();
		categoria.setDscCategoria(request.nome());
		categoria.setCorLetra(request.corLetra());
		categoria.setCorFundo(request.corFundo());

		return categoria;
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

	private void validarItemDeExclusao(Long ideCategoria) {

		List<String> itensVinculadosCategoria = itemRepository
				.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(ideCategoria);

		if (!itensVinculadosCategoria.isEmpty()) {
			throw new CategoriaVinculadaItensAtivosException(itensVinculadosCategoria);
		}

	}

	private void validarListaDeExclusao(List<Long> idesCategoria, List<Categoria> categoriasparaExclusao) {

		if (categoriasparaExclusao.size() != idesCategoria.size()) {
			Set<Long> idsEncontrados = categoriasparaExclusao.stream().map(Categoria::getIdeCategoria)
					.collect(Collectors.toSet());

			List<Long> idsNaoEncontrados = idesCategoria.stream().filter(id -> !idsEncontrados.contains(id))
					.collect(Collectors.toList());

			throw new ListaCategoriaInexistenteException(idsNaoEncontrados);
		}

		List<String> categoriasVinculadasAItens = new ArrayList<String>();

		for (Categoria categoria : categoriasparaExclusao) {
			List<String> itensVinculadosCategoria = new ArrayList<String>();
			itensVinculadosCategoria = itemRepository
					.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(categoria.getIdeCategoria());

			if (!itensVinculadosCategoria.isEmpty()) {
				categoriasVinculadasAItens
						.add(categoria.getDscCategoria() + ": " + itensVinculadosCategoria.toString());
			}

		}

		if (!categoriasVinculadasAItens.isEmpty()) {
			throw new CategoriaVinculadaItensAtivosException(categoriasVinculadasAItens);
		}

	}

	private ExclusaoResponseDTO objetoRespostaDeExclusao(Integer quantidadeExcluida, LocalDateTime dataExclusao,
			List<Categoria> categoriasExcluidas) {
		return new ExclusaoResponseDTO(quantidadeExcluida, dataExclusao,
				categoriasExcluidas.stream().map(Categoria::getDscCategoria).collect(Collectors.toList()));
	}

}
