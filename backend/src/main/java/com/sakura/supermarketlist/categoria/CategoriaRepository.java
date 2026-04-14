package com.sakura.supermarketlist.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

	boolean existsByDscCategoriaAndIndAtivoTrue(String nome);

}
