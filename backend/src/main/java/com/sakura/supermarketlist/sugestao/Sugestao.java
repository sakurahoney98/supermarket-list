package com.sakura.supermarketlist.sugestao;

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
@Table(name = "sugestao")
public class Sugestao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ide_sugestao", nullable = false, unique = true)
	private Long ideSugestao;
	
	@NotBlank(message = "Nome é obrigatório")
	@Column(name = "nome_sugestao", nullable = false)
	String nomeSugestao;
	
	@NotBlank(message = "Cor da letra é obrigatório")
	@Column(name = "cor_letra", nullable = false)
	String corLetra;
	
	@NotBlank(message = "Cor do fundo é obrigatório")
	@Column(name = "cor_fundo", nullable = false)
	String corFundo;
	
	@Column(name="dtc_criacao", nullable = false)
	 @CreationTimestamp
	 private LocalDateTime dtcCriacao;
	 
	 @Column(name="dtc_exclusao")
	 private LocalDateTime dtcExclusao;
	 
	 @Column(name="ind_ativo", nullable = false)
	 private boolean indAtivo = true;

	 public Sugestao() {
		
	 }

	 public Sugestao(Long ideSugestao, @NotBlank(message = "Nome é obrigatório") String nomeSugestao,
			@NotBlank(message = "Cor da letra é obrigatório") String corLetra,
			@NotBlank(message = "Cor do fundo é obrigatório") String corFundo, LocalDateTime dtcCriacao,
			LocalDateTime dtcExclusao, boolean indAtivo) {
		super();
		this.ideSugestao = ideSugestao;
		this.nomeSugestao = nomeSugestao;
		this.corLetra = corLetra;
		this.corFundo = corFundo;
		this.dtcCriacao = dtcCriacao;
		this.dtcExclusao = dtcExclusao;
		this.indAtivo = indAtivo;
	 }

	 public Long getIdeSugestao() {
		 return ideSugestao;
	 }

	 public void setIdeSugestao(Long ideSugestao) {
		 this.ideSugestao = ideSugestao;
	 }

	 public String getNomeSugestao() {
		 return nomeSugestao;
	 }

	 public void setNomeSugestao(String nomeSugestao) {
		 this.nomeSugestao = nomeSugestao;
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
