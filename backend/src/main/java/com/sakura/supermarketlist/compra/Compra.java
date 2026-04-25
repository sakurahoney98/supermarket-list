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
	private LocalDate dataCompra;

	@Column(name = "valor_total", precision = 10, scale = 2)
	private BigDecimal valorTotal;

	public Compra() {
		super();
		
	}

	public Compra(Long ideCompra, @NotNull(message = "Data da compra é obrigatória") LocalDate dataCompra,
			BigDecimal valorTotal) {
		super();
		this.ideCompra = ideCompra;
		this.dataCompra = dataCompra;
		this.valorTotal = valorTotal;
	}

	public Long getIdeCompra() {
		return ideCompra;
	}

	public void setIdeCompra(Long ideCompra) {
		this.ideCompra = ideCompra;
	}

	public LocalDate getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(LocalDate dataCompra) {
		this.dataCompra = dataCompra;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	
	
	
	

}
