package com.sakura.supermarketlist.item;

import java.time.LocalDateTime;
import java.util.List;

public record ItemExcluidoResponse(
		int quantidadeExcluida,
		LocalDateTime dataExclusao,
		List<String> nomesItensExcluidos
		) {

}
