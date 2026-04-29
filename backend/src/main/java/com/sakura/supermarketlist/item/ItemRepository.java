package com.sakura.supermarketlist.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository  extends JpaRepository<Item, Long> {
	
	@Query("SELECT i.nomeItem FROM Item i WHERE i.categoria.ideCategoria = :categoriaId AND i.indAtivo = true")
    List<String> findNomesByCategoriaIdeCategoriaAndIndAtivoTrue(@Param("categoriaId") Long categoriaId);
	
	boolean existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrue(String nome, Long ideCategoria);
	
	boolean existsByNomeItemAndCategoriaIdeCategoriaAndIndAtivoTrueAndIdeItemNot(String nome, Long ideCategoria, Long ideItem);
	
	Optional <Item> findByIdeItemAndIndAtivoTrue(Long ideItem);
	
	List <Item> findAllByIdeItemInAndIndAtivoTrue(List<Long> idesItens);
	
	List<Item> findByIndAtivoTrueOrderByCategoriaIdeCategoriaAscNomeItemAsc();
	
	List<Item> findByIndAtivoTrueOrderByDtcCriacaoDesc();
	
	List <Item> findByNomeItemContainingIgnoreCaseAndIndAtivoTrueOrderByNomeItemAsc(String termo);
	
	List <Item> findByCategoriaIdeCategoriaAndIndAtivoTrueOrderByNomeItemAsc(Long ideCategoria);
		
	List <Item> findByCategoriaIdeCategoriaAndNomeItemContainingIgnoreCaseAndIndAtivoTrueOrderByNomeItemAsc(Long ideCategoria, String termo);
	
	Integer countByIndAtivoTrue();
	
	Integer countByQuantidadeEstoqueAndIndAtivoTrue (Integer quantidadeEstoque);
	

	

}
