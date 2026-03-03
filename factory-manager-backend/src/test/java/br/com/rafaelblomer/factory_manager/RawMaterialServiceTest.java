package br.com.rafaelblomer.factory_manager;

import br.com.rafaelblomer.factory_manager.business.RawMaterialService;
import br.com.rafaelblomer.factory_manager.business.converters.RawMaterialConverter;
import br.com.rafaelblomer.factory_manager.business.dtos.RawMaterialRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.RawMaterialResponseDTO;
import br.com.rafaelblomer.factory_manager.business.exceptions.DuplicateCodeException;
import br.com.rafaelblomer.factory_manager.business.exceptions.ResourceNotFoundException;
import br.com.rafaelblomer.factory_manager.infrastructure.model.RawMaterial;
import br.com.rafaelblomer.factory_manager.infrastructure.repositories.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock
    private RawMaterialRepository repository;

    @Mock
    private RawMaterialConverter converter;

    @InjectMocks
    private RawMaterialService rawMaterialService;

    private RawMaterial entity;
    private RawMaterialRequestDTO requestDTO;
    private RawMaterialResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        entity = new RawMaterial(1L, "RM001", "Wheat Flour", 1000.0, "g");
        requestDTO = new RawMaterialRequestDTO("RM001", "Wheat Flour", 1000.0, "g");
        responseDTO = new RawMaterialResponseDTO(1L, "RM001", "Wheat Flour", 1000.0, "g");
    }

    @Test
    @DisplayName("Should create a raw material successfully")
    void createRawMaterial_shouldCreateSuccessfully() {
        when(repository.existsByCode(requestDTO.code())).thenReturn(false);
        when(converter.toEntity(requestDTO)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(converter.toResponseDTO(entity)).thenReturn(responseDTO);

        RawMaterialResponseDTO result = rawMaterialService.createRawMaterial(requestDTO);

        assertNotNull(result);
        assertEquals("RM001", result.code());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should throw DuplicateCodeException when code already exists")
    void createRawMaterial_shouldThrowWhenCodeAlreadyExists() {
        when(repository.existsByCode(requestDTO.code())).thenReturn(true);

        assertThrows(DuplicateCodeException.class,
                () -> rawMaterialService.createRawMaterial(requestDTO));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should return raw material by ID successfully")
    void findById_shouldReturnSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(converter.toResponseDTO(entity)).thenReturn(responseDTO);

        RawMaterialResponseDTO result = rawMaterialService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    void findById_shouldThrowWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> rawMaterialService.findById(99L));
    }

    @Test
    @DisplayName("Should return all raw materials")
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(converter.toResponseDTO(entity)).thenReturn(responseDTO);

        List<RawMaterialResponseDTO> result = rawMaterialService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should delete raw material successfully")
    void deleteRawMaterial_shouldDeleteSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> rawMaterialService.deleteRawMaterial(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent raw material")
    void deleteRawMaterial_shouldThrowWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> rawMaterialService.deleteRawMaterial(99L));

        verify(repository, never()).deleteById(any());
    }
}
