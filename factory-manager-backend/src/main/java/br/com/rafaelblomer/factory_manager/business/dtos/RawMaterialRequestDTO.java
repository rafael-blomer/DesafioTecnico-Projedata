package br.com.rafaelblomer.factory_manager.business.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RawMaterialRequestDTO(
        @NotBlank(message = "Code is required") String code,
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Stock quantity is required") @Min(0) Double stockQuantity,
        @NotBlank(message = "Unit is required") String unit
) {
}
