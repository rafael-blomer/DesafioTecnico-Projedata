package br.com.rafaelblomer.factory_manager.business.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDTO(
        Long id,
        String code,
        String name,
        BigDecimal price,
        List<ProductIngredientResponseDTO> ingredients
) {
}
