package com.sakura.supermarketlist.item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository  extends JpaRepository<Item, Long> {
	
	@Query("SELECT i.nomeItem FROM Item i WHERE i.categoria.ideCategoria = :categoriaId AND i.indAtivo = true")
    List<String> findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(@Param("categoriaId") Long categoriaId);
	
	boolean existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue (String nome, Long ideCategoria);
	

}
