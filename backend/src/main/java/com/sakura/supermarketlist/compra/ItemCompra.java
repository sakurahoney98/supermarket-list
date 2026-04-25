package com.sakura.supermarketlist.compra;

import java.math.BigDecimal;

import com.sakura.supermarketlist.item.dto.ItemResponseDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "item_compra")
public class ItemCompra {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ide_item_compra", nullable = false, unique = true)
	private Long ideItemCompra;

	@NotNull(message = "Vinculação a uma compra é necessária.")
	@Column(name = "ide_compra", nullable = false)
	private Compra compra;

	@NotNull(message = "Informação do item é necessária.")
	@Column(name = "ide_item")
	private ItemResponseDTO item;
	
	@NotNull(message = "Informação de quantidade de compra é necessária.")
	@Column(name = "quantidade")
	private Integer quantidade;
	
	@Column(name = "preco")
	private BigDecimal preco;
	
	@Column(name = "marca")
	private BigDecimal marca;

	public ItemCompra() {
		super();
		
	}

	public ItemCompra(Long ideItemCompra, @NotNull(message = "Vinculação a uma compra é necessária.") Compra compra,
			@NotNull(message = "Informação do item é necessária.") ItemResponseDTO item,
			@NotNull(message = "Informação de quantidade de compra é necessária.") Integer quantidade, BigDecimal preco,
			BigDecimal marca) {
		super();
		this.ideItemCompra = ideItemCompra;
		this.compra = compra;
		this.item = item;
		this.quantidade = quantidade;
		this.preco = preco;
		this.marca = marca;
	}

	public Long getIdeItemCompra() {
		return ideItemCompra;
	}

	public void setIdeItemCompra(Long ideItemCompra) {
		this.ideItemCompra = ideItemCompra;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public ItemResponseDTO getItem() {
		return item;
	}

	public void setItem(ItemResponseDTO item) {
		this.item = item;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public BigDecimal getMarca() {
		return marca;
	}

	public void setMarca(BigDecimal marca) {
		this.marca = marca;
	}
	
	
	
	
	
	
	


}
