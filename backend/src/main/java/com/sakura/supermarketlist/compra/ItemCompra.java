package com.sakura.supermarketlist.compra;

import java.math.BigDecimal;

import com.sakura.supermarketlist.item.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	@ManyToOne
	@JoinColumn(name = "ide_compra", nullable = false)
	private Compra compra;

	@NotNull(message = "Informação do item é necessária.")
	@ManyToOne
	@JoinColumn(name = "ide_item", nullable = false)
	private Item item;
	
	@NotNull(message = "Informação de quantidade de compra é necessária.")
	@Column(name = "quantidade",  nullable = false)
	private Integer quantidade;
	
	@Column(name = "preco", precision = 10, scale = 2)
	private BigDecimal preco;
	
	@Column(name = "marca", length = 100)
	private String marca;

	public ItemCompra() {
		super();
		
	}

	public ItemCompra(Long ideItemCompra, @NotNull(message = "Vinculação a uma compra é necessária.") Compra compra,
			@NotNull(message = "Informação do item é necessária.") Item item,
			@NotNull(message = "Informação de quantidade de compra é necessária.") Integer quantidade, BigDecimal preco,
			String marca) {
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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
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

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	
	
	


}
