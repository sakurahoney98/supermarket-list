package com.sakura.supermarketlist.categoria;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categoria")
public class Categoria {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ide_categoria" ,nullable = false, unique = true)
	private Long ideCategoria;
	
	 @NotBlank(message = "Nome é obrigatório")
	 @Column(name="dsc_categoria", nullable = false, unique = true)
	 private String dscCategoria;
	 
	 @NotBlank(message = "Cor da letra é obrigatório")
	 @Column(name="cor_letra", nullable = false)
	 private String corLetra;
	 
	 @NotBlank(message = "Cor do fundo é obrigatório")
	 @Column(name="cor_fundo", nullable = false)
	 private String corFundo;
	 
	 @Column(name="dtc_criacao", nullable = false)
	 @CreationTimestamp
	 private LocalDateTime dtcCriacao;
	 
	 @Column(name="dtc_exclusao")
	 private LocalDateTime dtcExclusao;
	 
	 @Column(name="ind_ativo", nullable = false)
	 private boolean indAtivo = true;
	 
	 

	 public Categoria() {
		
	}



	 public Categoria(Long ideCategoria, @NotBlank(message = "Nome é obrigatório") String dscCategoria,
			@NotBlank(message = "Cor da letra é obrigatório") String corLetra,
			@NotBlank(message = "Cor do fundo é obrigatório") String corFundo, LocalDateTime dtcCriacao,
			LocalDateTime dtcExclusao, boolean indAtivo) {
		super();
		this.ideCategoria = ideCategoria;
		this.dscCategoria = dscCategoria;
		this.corLetra = corLetra;
		this.corFundo = corFundo;
		this.dtcCriacao = dtcCriacao;
		this.dtcExclusao = dtcExclusao;
		this.indAtivo = indAtivo;
	 }



	 public Long getIdeCategoria() {
		 return ideCategoria;
	 }



	 public void setIdeCategoria(Long ideCategoria) {
		 this.ideCategoria = ideCategoria;
	 }



	 public String getDscCategoria() {
		 return dscCategoria;
	 }



	 public void setDscCategoria(String dscCategoria) {
		 this.dscCategoria = dscCategoria;
	 }



	 public String getCorLetra() {
		 return corLetra;
	 }



	 public void setCorLetra(String corLetra) {
		 this.corLetra = corLetra;
	 }



	 public String getCorFundo() {
		 return corFundo;
	 }



	 public void setCorFundo(String corFundo) {
		 this.corFundo = corFundo;
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
