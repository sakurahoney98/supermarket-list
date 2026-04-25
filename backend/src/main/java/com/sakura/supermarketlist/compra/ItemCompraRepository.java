package com.sakura.supermarketlist.compra;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long>{
	
	Integer countByCompraIdeCompra(Long ideCompra);

}
