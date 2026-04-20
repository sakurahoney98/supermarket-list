package com.sakura.supermarketlist.categoria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakura.supermarketlist.categoria.exception.CategoriaDuplicadaException;
import com.sakura.supermarketlist.categoria.exception.CategoriaInexistenteException;
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

		Categoria categoria = conversaoDTOParaEntidade(request);

		Categoria objeto = repository.save(categoria);

		return conversaoEntidadeParaDTO(objeto);

	}

	public boolean excluirCategoria(Long ideCategoria) {
		Categoria categoria = repository.findByIdeCategoriaAndIndAtivoTrue(ideCategoria)
				.orElseThrow(() -> new CategoriaInexistenteException());

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

	public boolean excluirCategoria(List<Long> listaCategorias) {

		List<String> categoriasComBloqueioDeExclusao = new ArrayList<String>();

		for (Long ideCategoria : listaCategorias) {
			Categoria categoria = repository.findByIdeCategoriaAndIndAtivoTrue(ideCategoria)
					.orElseThrow(() -> new CategoriaInexistenteException());

			List<String> itensVinculadosCategoria = new ArrayList<String>();
			itensVinculadosCategoria = itemRepository.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(ideCategoria);

			if (isExclusaoPermitida(itensVinculadosCategoria)) {

				categoria.setIndAtivo(false);
				categoria.setDtcExclusao(LocalDateTime.now());

				repository.save(categoria);

			} else {
				categoriasComBloqueioDeExclusao.add(categoria.getDscCategoria() + ": " + itensVinculadosCategoria.toString());

			}

		}

		if (!categoriasComBloqueioDeExclusao.isEmpty()) {
			throw new CategoriaVinculadaItensAtivosException(categoriasComBloqueioDeExclusao);
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

	private Categoria conversaoDTOParaEntidade(CategoriaRequestDTO request) {
		Categoria categoria = new Categoria();
		categoria.setDscCategoria(request.nome());
		categoria.setCorLetra(request.corLetra());
		categoria.setCorFundo(request.corFundo());

		return categoria;
	}

	private CategoriaResponseDTO conversaoEntidadeParaDTO(Categoria objeto) {
		CategoriaResponseDTO dto = new CategoriaResponseDTO(
				objeto.getIdeCategoria(), 
				objeto.getDscCategoria(),
				objeto.getCorLetra(), 
				objeto.getCorFundo(),
				objeto.isIndAtivo());

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
