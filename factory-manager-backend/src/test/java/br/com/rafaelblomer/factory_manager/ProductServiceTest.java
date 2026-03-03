package br.com.rafaelblomer.factory_manager;

import br.com.rafaelblomer.factory_manager.business.ProductService;
import br.com.rafaelblomer.factory_manager.business.converters.ProductConverter;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductIngredientRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductResponseDTO;
import br.com.rafaelblomer.factory_manager.business.exceptions.DuplicateCodeException;
import br.com.rafaelblomer.factory_manager.business.exceptions.InvalidProductCompositionException;
import br.com.rafaelblomer.factory_manager.business.exceptions.ResourceNotFoundException;
import br.com.rafaelblomer.factory_manager.infrastructure.model.Product;
import br.com.rafaelblomer.factory_manager.infrastructure.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductConverter converter;

    @InjectMocks
    private ProductService productService;

    private Product entity;
    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        ProductIngredientRequestDTO ingredient = new ProductIngredientRequestDTO(1L, 500.0);

        requestDTO = new ProductRequestDTO("PR001", "Chocolate Cake", new BigDecimal("45.00"), List.of(ingredient));
        entity = new Product();
        entity.setId(1L);
        entity.setCode("PR001");
        entity.setName("Chocolate Cake");
        entity.setPrice(new BigDecimal("45.00"));

        responseDTO = new ProductResponseDTO(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"), List.of());
    }

    @Test
    @DisplayName("Should create a product successfully")
    void createProduct_shouldCreateSuccessfully() {
        when(repository.existsByCode(requestDTO.code())).thenReturn(false);
        when(converter.toEntity(requestDTO)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(converter.toResponseDTO(entity)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.createProduct(requestDTO);

        assertNotNull(result);
        assertEquals("PR001", result.code());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should throw DuplicateCodeException when code already exists")
    void createProduct_shouldThrowWhenCodeAlreadyExists() {
        when(repository.existsByCode(requestDTO.code())).thenReturn(true);

        assertThrows(DuplicateCodeException.class,
                () -> productService.createProduct(requestDTO));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidProductCompositionException when ingredients list is empty")
    void createProduct_shouldThrowWhenIngredientsEmpty() {
        ProductRequestDTO noIngredients = new ProductRequestDTO(
                "PR002", "Empty Product", new BigDecimal("10.00"), List.of()
        );

        when(repository.existsByCode(noIngredients.code())).thenReturn(false);

        assertThrows(InvalidProductCompositionException.class,
                () -> productService.createProduct(noIngredients));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidProductCompositionException when ingredients list is null")
    void createProduct_shouldThrowWhenIngredientsNull() {
        ProductRequestDTO nullIngredients = new ProductRequestDTO(
                "PR002", "Null Ingredients", new BigDecimal("10.00"), null
        );

        when(repository.existsByCode(nullIngredients.code())).thenReturn(false);

        assertThrows(InvalidProductCompositionException.class,
                () -> productService.createProduct(nullIngredients));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should return product by ID successfully")
    void findById_shouldReturnSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(converter.toResponseDTO(entity)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.findById(1L);

        assertNotNull(result);
        assertEquals("PR001", result.code());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    void findById_shouldThrowWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.findById(99L));
    }

    @Test
    @DisplayName("Should return all products")
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(converter.toResponseDTO(entity)).thenReturn(responseDTO);

        List<ProductResponseDTO> result = productService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should delete product successfully")
    void deleteProduct_shouldDeleteSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent product")
    void deleteProduct_shouldThrowWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(99L));

        verify(repository, never()).deleteById(any());
    }
}
