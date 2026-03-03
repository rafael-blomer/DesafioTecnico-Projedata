package br.com.rafaelblomer.factory_manager.business.converters;

import br.com.rafaelblomer.factory_manager.business.dtos.ProductIngredientRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductIngredientResponseDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductResponseDTO;
import br.com.rafaelblomer.factory_manager.infrastructure.model.Product;
import br.com.rafaelblomer.factory_manager.infrastructure.model.ProductIngredient;
import br.com.rafaelblomer.factory_manager.infrastructure.model.RawMaterial;
import br.com.rafaelblomer.factory_manager.infrastructure.repositories.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductConverter {

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    public Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setCode(dto.code());
        product.setName(dto.name());
        product.setPrice(dto.price());

        List<ProductIngredient> ingredients = dto.ingredients().stream()
                .map(ingredientDTO -> toIngredientEntity(ingredientDTO, product))
                .collect(Collectors.toList());

        product.setIngredients(ingredients);
        return product;
    }

    public ProductResponseDTO toResponseDTO(Product entity) {
        List<ProductIngredientResponseDTO> ingredientDTOs = entity.getIngredients().stream()
                .map(this::toIngredientResponseDTO)
                .collect(Collectors.toList());

        return new ProductResponseDTO(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getPrice(),
                ingredientDTOs
        );
    }

    private ProductIngredient toIngredientEntity(ProductIngredientRequestDTO dto, Product product) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(dto.rawMaterialId())
                .orElseThrow(() -> new RuntimeException("Raw material not found: " + dto.rawMaterialId()));

        ProductIngredient ingredient = new ProductIngredient();
        ingredient.setProduct(product);
        ingredient.setRawMaterial(rawMaterial);
        ingredient.setQuantityRequired(dto.quantityRequired());
        return ingredient;
    }

    private ProductIngredientResponseDTO toIngredientResponseDTO(ProductIngredient entity) {
        return new ProductIngredientResponseDTO(
                entity.getRawMaterial().getId(),
                entity.getRawMaterial().getName(),
                entity.getRawMaterial().getUnit(),
                entity.getQuantityRequired()
        );
    }
}