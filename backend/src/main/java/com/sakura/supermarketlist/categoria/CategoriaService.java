package com.sakura.supermarketlist.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
	@Autowired
	CategoriaRepository repository;
	
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

}
