package br.com.rafaelblomer.factory_manager.business.dtos;

public record RawMaterialResponseDTO(
        Long id,
        String code,
        String name,
        Double stockQuantity,
        String unit
) {
}
