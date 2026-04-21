package com.sakura.supermarketlist.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AtualizacaoEstoqueRequestDTO(
		@NotNull(message = "ID do item é obrigatório")
	    @Positive(message = "ID do item deve ser positivo")
	    Long ideItem,
	    
	    @NotNull(message = "Quantidade atual é obrigatória")
	    @Min(value = 0, message = "Quantidade atual não pode ser negativa")
	    Integer quantidadeAtual,
	    
	    @NotNull(message = "Quantidade nova é obrigatória")
	    @Min(value = 0, message = "Quantidade nova não pode ser negativa")
	    Integer quantidadeNova
		) {

}
