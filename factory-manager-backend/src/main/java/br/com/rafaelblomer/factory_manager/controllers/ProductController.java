package br.com.rafaelblomer.factory_manager.controllers;

import br.com.rafaelblomer.factory_manager.business.ProductService;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Endpoints for managing products and their compositions")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "Create a product", description = "Registers a new product with its ingredient composition")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or empty ingredients list"),
            @ApiResponse(responseCode = "404", description = "One or more raw materials not found"),
            @ApiResponse(responseCode = "409", description = "Code already exists")
    })
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find product by ID", description = "Returns a single product with its full composition")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping
    @Operation(summary = "List all products", description = "Returns all products with their compositions")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Updates the data and/or composition of an existing product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error in fields"),
            @ApiResponse(responseCode = "404", description = "Product or raw material not found")
    })
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id,
                                                     @RequestBody @Valid ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Removes a product by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
