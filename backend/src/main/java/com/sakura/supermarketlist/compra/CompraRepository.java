package com.sakura.supermarketlist.compra;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<Compra, Long>{
	
	boolean existsByDataCompra(LocalDate data);
	
	List<Compra> findByDataCompraOrderByIdeCompra(LocalDate data);

}
