package com.sakura.supermarketlist.compra;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sakura.supermarketlist.relatorio.dto.RelatorioConsultaDTO;
import com.sakura.supermarketlist.relatorio.dto.RelatorioMensalResponseDTO;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long> {

	Integer countByCompraIdeCompra(Long ideCompra);

	@Query("""
			    SELECT NEW com.sakura.supermarketlist.relatorio.dto.RelatorioMensalResponseDTO(
			     i.nomeItem,
			        ic.marca,
			        ic.preco,
			        ic.quantidade
			    ) 
			    FROM ItemCompra ic
			    JOIN ic.compra c
			    JOIN ic.item i
			    WHERE YEAR(c.dataCompra) = :ano
			      AND MONTH(c.dataCompra) = :mes
			    ORDER BY i.nomeItem ASC
			""")
	List<RelatorioMensalResponseDTO> buscarItensPorMesEAno(@Param("ano") int ano, @Param("mes") int mes);
	
	@Query("""
		    SELECT NEW com.sakura.supermarketlist.relatorio.dto.RelatorioConsultaDTO(
		        c.dataCompra,
		        ic.marca,
		        ic.preco,
		        ic.quantidade
		    )
		    FROM ItemCompra ic
		    JOIN ic.compra c
		    WHERE ic.item.ideItem = :ideItem
		      AND c.dataCompra BETWEEN :inicio AND :fim
		    ORDER BY c.dataCompra
		""")
	List<RelatorioConsultaDTO> buscarGastoPorPeriodo(@Param("ideItem") Long ideItem, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

}
