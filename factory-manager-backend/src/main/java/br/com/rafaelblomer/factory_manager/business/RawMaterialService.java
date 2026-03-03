package br.com.rafaelblomer.factory_manager.business;

import br.com.rafaelblomer.factory_manager.business.converters.RawMaterialConverter;
import br.com.rafaelblomer.factory_manager.business.dtos.RawMaterialRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.RawMaterialResponseDTO;
import br.com.rafaelblomer.factory_manager.business.exceptions.DuplicateCodeException;
import br.com.rafaelblomer.factory_manager.business.exceptions.ResourceNotFoundException;
import br.com.rafaelblomer.factory_manager.infrastructure.model.RawMaterial;
import br.com.rafaelblomer.factory_manager.infrastructure.repositories.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RawMaterialService {

    @Autowired
    private RawMaterialRepository repository;

    @Autowired
    private RawMaterialConverter converter;

    /**
     * Creates a new raw material.
     *
     * @param dto DTO with raw material data
     * @return RawMaterialResponseDTO with the created raw material data
     * Flow:
     * - Validates that the code is not already in use
     * - Converts DTO to entity
     * - Saves to repository
     */
    @Transactional
    public RawMaterialResponseDTO createRawMaterial(RawMaterialRequestDTO dto) {
        validateUniqueCode(dto.code());
        RawMaterial entity = converter.toEntity(dto);
        repository.save(entity);
        return converter.toResponseDTO(entity);
    }

    /**
     * Updates an existing raw material.
     *
     * @param id  ID of the raw material to update
     * @param dto DTO with the new data
     * @return Updated RawMaterialResponseDTO
     * Flow:
     * - Finds raw material by ID
     * - Applies non-null changes
     * - Saves to repository
     */
    @Transactional
    public RawMaterialResponseDTO updateRawMaterial(Long id, RawMaterialRequestDTO dto) {
        RawMaterial existing = findRawMaterialById(id);
        updateRawMaterialData(existing, dto);
        return converter.toResponseDTO(repository.save(existing));
    }

    /**
     * Finds a raw material by its ID.
     *
     * @param id ID of the raw material
     * @return RawMaterialResponseDTO found
     */
    @Transactional(readOnly = true)
    public RawMaterialResponseDTO findById(Long id) {
        return converter.toResponseDTO(findRawMaterialById(id));
    }

    /**
     * Returns all raw materials.
     *
     * @return List of RawMaterialResponseDTO
     */
    @Transactional(readOnly = true)
    public List<RawMaterialResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(converter::toResponseDTO)
                .toList();
    }

    /**
     * Deletes a raw material by its ID.
     *
     * @param id ID of the raw material to delete
     *           - Throws ResourceNotFoundException if not found
     *           - DataIntegrityViolationException is handled by ResourceExceptionHandler
     *           if the raw material is linked to any product
     */
    @Transactional
    public void deleteRawMaterial(Long id) {
        findRawMaterialById(id);
        repository.deleteById(id);
    }

    // UTILITY METHODS

    /**
     * Finds a raw material entity by ID.
     *
     * @param id ID of the raw material
     * @return RawMaterial entity
     * @throws ResourceNotFoundException if not found
     */
    public RawMaterial findRawMaterialById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + id));
    }

    /**
     * Validates that the given code is not already in use.
     *
     * @param code Code to validate
     * @throws DuplicateCodeException if code already exists
     */
    private void validateUniqueCode(String code) {
        if (repository.existsByCode(code))
            throw new DuplicateCodeException("Raw material with code '" + code + "' already exists.");
    }

    /**
     * Updates the fields of an existing raw material.
     *
     * @param existing Current entity
     * @param dto      DTO with new values
     */
    private void updateRawMaterialData(RawMaterial existing, RawMaterialRequestDTO dto) {
        if (dto.name() != null)
            existing.setName(dto.name());
        if (dto.stockQuantity() != null)
            existing.setStockQuantity(dto.stockQuantity());
        if (dto.unit() != null)
            existing.setUnit(dto.unit());
    }
}
