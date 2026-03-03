package br.com.rafaelblomer.factory_manager.business.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ProductionSuggestionResponseDTO(
        List<ProductionItemResponseDTO> items,
        BigDecimal totalValue
) {
}
