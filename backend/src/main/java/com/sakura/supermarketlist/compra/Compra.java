package com.sakura.supermarketlist.compra;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "compra")
public class Compra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ide_compra", nullable = false, unique = true)
	private Long ideCompra;

	@NotNull(message = "Data da compra é obrigatória")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "data_compra", nullable = false)
	private LocalDate dataUltimaCompra;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	public Compra() {
		super();
		
	}

	public Compra(Long ideCompra, @NotNull(message = "Data da compra é obrigatória") LocalDate dataUltimaCompra,
			BigDecimal valorTotal) {
		super();
		this.ideCompra = ideCompra;
		this.dataUltimaCompra = dataUltimaCompra;
		this.valorTotal = valorTotal;
	}

	public Long getIdeCompra() {
		return ideCompra;
	}

	public void setIdeCompra(Long ideCompra) {
		this.ideCompra = ideCompra;
	}

	public LocalDate getDataUltimaCompra() {
		return dataUltimaCompra;
	}

	public void setDataUltimaCompra(LocalDate dataUltimaCompra) {
		this.dataUltimaCompra = dataUltimaCompra;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	
	
	

}
