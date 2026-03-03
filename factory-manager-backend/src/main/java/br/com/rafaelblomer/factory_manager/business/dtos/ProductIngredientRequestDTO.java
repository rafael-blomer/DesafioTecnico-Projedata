package br.com.rafaelblomer.factory_manager.business.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductIngredientRequestDTO(
        @NotNull(message = "Raw material ID is required") Long rawMaterialId,
        @NotNull(message = "Quantity is required") @Min(0) Double quantityRequired
) {
}
