package com.sakura.supermarketlist.categoria;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

	boolean existsByDscCategoriaAndIndAtivoTrue(String nome);
	
	Optional <Categoria> findByIdeCategoriaAndIndAtivoTrue(Long ideCategoria);
	
	List <Categoria> findByIndAtivoTrueOrderByDscCategoriaAsc();
	
	List <Categoria> findByIndAtivoTrueOrderByDtcCriacaoDesc();
	
	List <Categoria> findByDscCategoriaContainingIgnoreCaseAndIndAtivoTrueOrderByDscCategoriaAsc(String termo);
	
	List<Categoria> findAllByIdeCategoriaInAndIndAtivoTrue(List<Long> idsCategorias);
	
	boolean existsByIndAtivoTrue();
	
	boolean existsByIdeCategoriaAndIndAtivoTrue(Long ideCategoria);
	

}
