package br.com.rafaelblomer.factory_manager.controllers;

import br.com.rafaelblomer.factory_manager.business.RawMaterialService;
import br.com.rafaelblomer.factory_manager.business.dtos.RawMaterialRequestDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.RawMaterialResponseDTO;
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
@RequestMapping("/api/raw-materials")
@Tag(name = "Raw Materials", description = "Endpoints for managing raw materials (inputs)")
public class RawMaterialController {

    @Autowired
    private RawMaterialService rawMaterialService;

    @PostMapping
    @Operation(summary = "Create a raw material", description = "Registers a new raw material in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Raw material created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error in fields"),
            @ApiResponse(responseCode = "409", description = "Code already exists")
    })
    public ResponseEntity<RawMaterialResponseDTO> create(@RequestBody @Valid RawMaterialRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rawMaterialService.createRawMaterial(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find raw material by ID", description = "Returns a single raw material by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Raw material found"),
            @ApiResponse(responseCode = "404", description = "Raw material not found")
    })
    public ResponseEntity<RawMaterialResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rawMaterialService.findById(id));
    }

    @GetMapping
    @Operation(summary = "List all raw materials", description = "Returns all raw materials registered in the system")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<List<RawMaterialResponseDTO>> findAll() {
        return ResponseEntity.ok(rawMaterialService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a raw material", description = "Updates the data of an existing raw material")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Raw material updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error in fields"),
            @ApiResponse(responseCode = "404", description = "Raw material not found")
    })
    public ResponseEntity<RawMaterialResponseDTO> update(@PathVariable Long id,
                                                         @RequestBody @Valid RawMaterialRequestDTO dto) {
        return ResponseEntity.ok(rawMaterialService.updateRawMaterial(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a raw material", description = "Removes a raw material by its ID. Fails if it is linked to any product.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Raw material deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Raw material not found"),
            @ApiResponse(responseCode = "409", description = "Raw material is linked to one or more products")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
