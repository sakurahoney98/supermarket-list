package com.sakura.supermarketlist.sugestao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SugestaoRepository extends JpaRepository<Sugestao, Long> {
	
	List<Sugestao> findByIndAtivoTrue();

}
