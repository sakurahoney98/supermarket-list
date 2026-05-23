package com.sakura.supermarketlist.compra;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sakura.supermarketlist.relatorio.dto.IntervaloAnosCompraDTO;

public interface CompraRepository extends JpaRepository<Compra, Long>{
	
	boolean existsByDataCompra(LocalDate data);
	
	List<Compra> findByDataCompraOrderByIdeCompra(LocalDate data);
	
	@Query("""
			SELECT NEW com.sakura.supermarketlist.relatorio.dto.IntervaloAnosCompraDTO(
			MIN(YEAR(c.dataCompra)),
			MAX(YEAR(c.dataCompra))
			)
			FROM Compra c
			""")
	IntervaloAnosCompraDTO buscarIntervaloAnos();

}
