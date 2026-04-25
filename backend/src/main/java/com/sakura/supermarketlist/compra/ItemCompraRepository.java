package com.sakura.supermarketlist.compra;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sakura.supermarketlist.relatorio.RelatorioMensalResponseDTO;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long> {

	Integer countByCompraIdeCompra(Long ideCompra);

	@Query("""
			    SELECT NEW com.sakura.supermarketlist.relatorio.RelatorioMensalResponseDTO(
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

}
