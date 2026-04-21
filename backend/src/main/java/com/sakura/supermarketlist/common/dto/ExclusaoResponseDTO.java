package com.sakura.supermarketlist.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExclusaoResponseDTO(
		int quantidadeExcluida,
		LocalDateTime dataExclusao,
		List<String> nomesItensExcluidos
		) {

}
