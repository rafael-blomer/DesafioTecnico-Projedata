package br.com.rafaelblomer.factory_manager.business;

import br.com.rafaelblomer.factory_manager.business.converters.ProductConverter;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductResponseDTO;
import br.com.rafaelblomer.factory_manager.business.exceptions.DuplicateCodeException;
import br.com.rafaelblomer.factory_manager.business.exceptions.InvalidProductCompositionException;
import br.com.rafaelblomer.factory_manager.business.exceptions.ResourceNotFoundException;
import br.com.rafaelblomer.factory_manager.infrastructure.model.Product;
import br.com.rafaelblomer.factory_manager.infrastructure.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductConverter converter;

    /**
     * Creates a new product with its ingredient composition.
     *
     * @param dto DTO with product data and ingredients list
     * @return ProductResponseDTO with the created product data
     * Flow:
     * - Validates that the code is not already in use
     * - Validates that the ingredients list is not empty
     * - Converts DTO to entity (resolves each raw material by ID)
     * - Saves to repository
     */
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        validateUniqueCode(dto.code());
        validateIngredients(dto);
        Product product = converter.toEntity(dto);
        repository.save(product);
        return converter.toResponseDTO(product);
    }

    /**
     * Updates an existing product's basic data.
     *
     * @param id  ID of the product to update
     * @param dto DTO with the new data
     * @return Updated ProductResponseDTO
     * Flow:
     * - Finds product by ID
     * - Applies non-null changes to basic fields
     * - If a new ingredients list is provided, replaces the current one
     * - Saves to repository
     */
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product existing = findProductById(id);
        updateProductData(existing, dto);
        return converter.toResponseDTO(repository.save(existing));
    }

    /**
     * Finds a product by its ID.
     *
     * @param id ID of the product
     * @return ProductResponseDTO found
     */
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        return converter.toResponseDTO(findProductById(id));
    }

    /**
     * Returns all products.
     *
     * @return List of ProductResponseDTO
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(converter::toResponseDTO)
                .toList();
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id ID of the product to delete
     * @throws ResourceNotFoundException if not found
     */
    @Transactional
    public void deleteProduct(Long id) {
        findProductById(id);
        repository.deleteById(id);
    }

    // UTILITY METHODS

    /**
     * Finds a product entity by ID.
     *
     * @param id ID of the product
     * @return Product entity
     * @throws ResourceNotFoundException if not found
     */
    public Product findProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    /**
     * Validates that the given code is not already in use.
     *
     * @param code Code to validate
     * @throws DuplicateCodeException if code already exists
     */
    private void validateUniqueCode(String code) {
        if (repository.existsByCode(code))
            throw new DuplicateCodeException("Product with code '" + code + "' already exists.");
    }

    /**
     * Validates that the product has at least one ingredient.
     *
     * @param dto ProductRequestDTO to validate
     * @throws InvalidProductCompositionException if ingredients list is null or empty
     */
    private void validateIngredients(ProductRequestDTO dto) {
        if (dto.ingredients() == null || dto.ingredients().isEmpty())
            throw new InvalidProductCompositionException("Product must have at least one ingredient.");
    }

    /**
     * Updates the fields of an existing product.
     *
     * @param existing Current entity
     * @param dto      DTO with new values
     *                 If a new ingredients list is provided, clears the current one and adds the new entries.
     */
    private void updateProductData(Product existing, ProductRequestDTO dto) {
        if (dto.name() != null)
            existing.setName(dto.name());
        if (dto.price() != null)
            existing.setPrice(dto.price());
        if (dto.ingredients() != null && !dto.ingredients().isEmpty()) {
            existing.getIngredients().clear();
            converter.toEntity(dto).getIngredients()
                    .forEach(ingredient -> {
                        ingredient.setProduct(existing);
                        existing.getIngredients().add(ingredient);
                    });
        }
    }
}
