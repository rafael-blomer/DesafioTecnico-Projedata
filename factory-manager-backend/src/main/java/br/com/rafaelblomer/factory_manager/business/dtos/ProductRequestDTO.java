package br.com.rafaelblomer.factory_manager.business.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequestDTO(
        @NotBlank(message = "Code is required") String code,
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Price is required") @DecimalMin("0.0") BigDecimal price,
        @NotEmpty(message = "Ingredients must not be empty") List<ProductIngredientRequestDTO> ingredients
) {
}
