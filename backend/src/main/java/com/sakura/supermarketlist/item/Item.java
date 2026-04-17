package com.sakura.supermarketlist.item;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.sakura.supermarketlist.categoria.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "item")
public class Item {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ide_item" ,nullable = false, unique = true)
	private Long ideItem;
	
	 @NotBlank(message = "Nome é obrigatório")
	 @Column(name="nome_item", nullable = false)
	 private String nomeItem;
	 
	 @Column(name="unidade_medida")
	 private String unidadeMedida;
	 
	 @NotNull(message = "A quantidade em estoque é obrigatório")
	 @Column(name="quantidade_estoque", nullable = false)
	 private Integer quantidadeEstoque;
	 
	 @NotNull(message = "O limite de compra de item é obrigatório")
	 @Column(name="limite_compra", nullable = false)
	 private Integer limiteCompra;
	 
	 @NotNull(message = "data da última compra do item é obrigatório")
	 @Column(name="data_ultima_compra", nullable = false)
	 private LocalDateTime dataUltimaCompra;
	 
	 @NotBlank(message = "A vinculação do item com uma categoria é obrigatório")
	 @ManyToOne
	 @JoinColumn(name = "ide_categoria", nullable = false)
	 private Categoria categoria;
	 
	 @NotNull(message = "Nome é obrigatório")
	 @Column(name="duracao_dias", nullable = false)
	 private Integer duracaoDias;
	 
	 @Column(name="dtc_criacao", nullable = false)
	 @CreationTimestamp
	 private LocalDateTime dtcCriacao;
	 
	 @Column(name="dtc_exclusao")
	 private LocalDateTime dtcExclusao;
	 
	 @Column(name="ind_ativo", nullable = false)
	 private boolean indAtivo = true;

	 
	 
	 
	 public Item() {

	}




	 public Item(Long ideItem, @NotBlank(message = "Nome é obrigatório") String nomeItem, String unidadeMedida,
			@NotBlank(message = "A quantidade em estoque é obrigatório") Integer quantidadeEstoque,
			@NotBlank(message = "O limite de compra de item é obrigatório") Integer limiteCompra,
			@NotBlank(message = "data da última compra do item é obrigatório") LocalDateTime dataUltimaCompra,
			@NotBlank(message = "A vinculação do item com uma categoria é obrigatório") Categoria categoria,
			@NotBlank(message = "Nome é obrigatório") Integer duracaoDias, LocalDateTime dtcCriacao,
			LocalDateTime dtcExclusao, boolean indAtivo) {
		super();
		this.ideItem = ideItem;
		this.nomeItem = nomeItem;
		this.unidadeMedida = unidadeMedida;
		this.quantidadeEstoque = quantidadeEstoque;
		this.limiteCompra = limiteCompra;
		this.dataUltimaCompra = dataUltimaCompra;
		this.categoria = categoria;
		this.duracaoDias = duracaoDias;
		this.dtcCriacao = dtcCriacao;
		this.dtcExclusao = dtcExclusao;
		this.indAtivo = indAtivo;
	 }




	 public Long getIdeItem() {
		 return ideItem;
	 }




	 public void setIdeItem(Long ideItem) {
		 this.ideItem = ideItem;
	 }




	 public String getNomeItem() {
		 return nomeItem;
	 }




	 public void setNomeItem(String nomeItem) {
		 this.nomeItem = nomeItem;
	 }




	 public String getUnidadeMedida() {
		 return unidadeMedida;
	 }




	 public void setUnidadeMedida(String unidadeMedida) {
		 this.unidadeMedida = unidadeMedida;
	 }




	 public Integer getQuantidadeEstoque() {
		 return quantidadeEstoque;
	 }




	 public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		 this.quantidadeEstoque = quantidadeEstoque;
	 }




	 public Integer getLimiteCompra() {
		 return limiteCompra;
	 }




	 public void setLimiteCompra(Integer limiteCompra) {
		 this.limiteCompra = limiteCompra;
	 }




	 public LocalDateTime getDataUltimaCompra() {
		 return dataUltimaCompra;
	 }




	 public void setDataUltimaCompra(LocalDateTime dataUltimaCompra) {
		 this.dataUltimaCompra = dataUltimaCompra;
	 }




	 public Categoria getCategoria() {
		 return categoria;
	 }




	 public void setCategoria(Categoria categoria) {
		 this.categoria = categoria;
	 }




	 public Integer getDuracaoDias() {
		 return duracaoDias;
	 }




	 public void setDuracaoDias(Integer duracaoDias) {
		 this.duracaoDias = duracaoDias;
	 }




	 public LocalDateTime getDtcCriacao() {
		 return dtcCriacao;
	 }




	 public void setDtcCriacao(LocalDateTime dtcCriacao) {
		 this.dtcCriacao = dtcCriacao;
	 }




	 public LocalDateTime getDtcExclusao() {
		 return dtcExclusao;
	 }




	 public void setDtcExclusao(LocalDateTime dtcExclusao) {
		 this.dtcExclusao = dtcExclusao;
	 }




	 public boolean isIndAtivo() {
		 return indAtivo;
	 }




	 public void setIndAtivo(boolean indAtivo) {
		 this.indAtivo = indAtivo;
	 }
	 
	 
	 
	 

}
