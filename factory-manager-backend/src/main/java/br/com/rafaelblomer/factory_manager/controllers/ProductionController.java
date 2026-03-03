package br.com.rafaelblomer.factory_manager.controllers;

import br.com.rafaelblomer.factory_manager.business.ProductionOptimizerService;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductionSuggestionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/production")
@Tag(name = "Production Optimizer", description = "Endpoint for production optimization based on current stock")
public class ProductionController {

    @Autowired
    private ProductionOptimizerService productionOptimizerService;

    @GetMapping("/optimize")
    @Operation(
            summary = "Optimize production",
            description = """
                    Analyzes the current stock and suggests which products to produce
                    in order to maximize the total sale value.
                    
                    The algorithm prioritizes products with the highest total return,
                    resolving conflicts when two products compete for the same raw material
                    by always favoring the one that generates more revenue.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suggestion generated successfully"),
            @ApiResponse(responseCode = "200", description = "No products can be produced with current stock — returns empty list with totalValue = 0")
    })
    public ResponseEntity<ProductionSuggestionResponseDTO> optimize() {
        return ResponseEntity.ok(productionOptimizerService.optimize());
    }
}
