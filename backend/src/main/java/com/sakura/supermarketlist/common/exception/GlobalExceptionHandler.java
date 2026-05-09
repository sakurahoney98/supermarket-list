package com.sakura.supermarketlist.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sakura.supermarketlist.categoria.exception.CategoriaDuplicadaException;
import com.sakura.supermarketlist.categoria.exception.CategoriaInexistenteException;
import com.sakura.supermarketlist.categoria.exception.CategoriaVinculadaItensAtivosException;
import com.sakura.supermarketlist.compra.exception.CompraInexistenteException;
import com.sakura.supermarketlist.compra.exception.ListaItensCompradosVaziaException;
import com.sakura.supermarketlist.item.exception.ItemDuplicadoNaCategoriaException;
import com.sakura.supermarketlist.item.exception.ItemInexistenteException;
import com.sakura.supermarketlist.item.exception.ListaItemInexistenteException;
import com.sakura.supermarketlist.item.exception.NenhumaCategoriaCadastradaException;
import com.sakura.supermarketlist.listacompra.exception.ListaDeComprasVaziaException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	
	@ExceptionHandler(CategoriaDuplicadaException.class)
	public ResponseEntity<String> handleCategoriaDuplicada(CategoriaDuplicadaException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(CategoriaInexistenteException.class)
	public ResponseEntity<String> handleCategoriaInexistente(CategoriaInexistenteException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(CategoriaVinculadaItensAtivosException.class)
	public ResponseEntity<String> handleCategoriaVinculadaItensAtivos(CategoriaVinculadaItensAtivosException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(CompraInexistenteException.class)
	public ResponseEntity<String> handleCompraInexistente(CompraInexistenteException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(ListaItensCompradosVaziaException.class)
	public ResponseEntity<String> handleListaItensCompradosVazia(ListaItensCompradosVaziaException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(ItemDuplicadoNaCategoriaException.class)
	public ResponseEntity<String> handleItemDuplicadoNaCategoria(ItemDuplicadoNaCategoriaException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(ItemInexistenteException.class)
	public ResponseEntity<String> handleItemInexistente(ItemInexistenteException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(ListaItemInexistenteException.class)
	public ResponseEntity<String> handleListaItemInexistente(ListaItemInexistenteException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(NenhumaCategoriaCadastradaException.class)
	public ResponseEntity<String> handleNenhumaCategoriaCadastrada(NenhumaCategoriaCadastradaException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(ListaDeComprasVaziaException.class)
	public ResponseEntity<String> handleListaDeComprasVazia(ListaDeComprasVaziaException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidacao(MethodArgumentNotValidException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleCorpoVazio(HttpMessageNotReadableException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<String> handleRotaInexistente(HttpRequestMethodNotSupportedException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleCaractereInvalido(IllegalArgumentException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleSemParametro(MissingServletRequestParameterException exception){
		return ResponseEntity.badRequest().body(exception.getMessage());
		
	}
	
	
	
	
	
	
	
	
	

}
