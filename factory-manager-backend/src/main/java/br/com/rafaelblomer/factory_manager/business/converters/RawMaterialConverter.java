package br.com.rafaelblomer.factory_manager.business.converters;

import br.com.rafaelblomer.factory_manager.business.dtos.RawMaterialRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.RawMaterialResponseDTO;
import br.com.rafaelblomer.factory_manager.infrastructure.model.RawMaterial;
import org.springframework.stereotype.Component;

@Component
public class RawMaterialConverter {

    public RawMaterial toEntity(RawMaterialRequestDTO dto) {
        RawMaterial entity = new RawMaterial();
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setStockQuantity(dto.stockQuantity());
        entity.setUnit(dto.unit());
        return entity;
    }

    public RawMaterialResponseDTO toResponseDTO(RawMaterial entity) {
        return new RawMaterialResponseDTO(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getStockQuantity(),
                entity.getUnit()
        );
    }
}
