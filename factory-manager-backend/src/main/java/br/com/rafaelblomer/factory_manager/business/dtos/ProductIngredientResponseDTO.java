package br.com.rafaelblomer.factory_manager.business.dtos;

public record ProductIngredientResponseDTO(
        Long rawMaterialId,
        String rawMaterialName,
        String unit,
        Double quantityRequired
) {
}
