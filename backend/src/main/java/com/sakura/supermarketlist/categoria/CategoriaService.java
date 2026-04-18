package com.sakura.supermarketlist.categoria;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakura.supermarketlist.item.ItemRepository;

@Service
public class CategoriaService {
	@Autowired
	CategoriaRepository repository;
	
	@Autowired
	ItemRepository itemRepository;
	
	public CategoriaResponseDTO cadastrarCategoria(CategoriaRequestDTO request) {
		
		if(repository.existsByDscCategoriaAndIndAtivoTrue(request.nome())) {
			throw new CategoriaDuplicadaException(request.nome());
			
		}
		
		Categoria categoria = new Categoria();
		categoria.setDscCategoria(request.nome());
		categoria.setCorLetra(request.corLetra());
		categoria.setCorFundo(request.corFundo());
		
		Categoria objeto = repository.save(categoria);
		
		return new CategoriaResponseDTO(
				objeto.getIdeCategoria(),
				objeto.getDscCategoria(),
				objeto.getCorLetra(),
				objeto.getCorFundo(),
				objeto.isIndAtivo()
		    );
		
	}
	
	public boolean excluirCategoria(Long ideCategoria){
		Categoria categoria = repository.findByIdeCategoriaAndIndAtivoTrue(ideCategoria).orElseThrow(() -> new RuntimeException("Identificador não existe ou a categoria já se encontra inativada"));
		
		List<String> itensVinculadosCategoria = itemRepository.findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(ideCategoria);
		
		if(isExclusaoPermitida(itensVinculadosCategoria)) {
			
			categoria.setIndAtivo(false);
			categoria.setDtcExclusao(LocalDateTime.now());
			
			repository.save(categoria);
			
		}else {
			throw new CategoriaVinculadaItensAtivosException(itensVinculadosCategoria);
		}
		
		return true;
		
		
	}
	
	private boolean isExclusaoPermitida(List <String> itensVinculadosCategoria) {
		
		return itensVinculadosCategoria.isEmpty();
		
	}
	
	
	

}
