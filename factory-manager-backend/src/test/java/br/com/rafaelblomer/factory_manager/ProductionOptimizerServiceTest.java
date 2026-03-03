package br.com.rafaelblomer.factory_manager;

import br.com.rafaelblomer.factory_manager.business.ProductionOptimizerService;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductionSuggestionResponseDTO;
import br.com.rafaelblomer.factory_manager.infrastructure.model.Product;
import br.com.rafaelblomer.factory_manager.infrastructure.model.ProductIngredient;
import br.com.rafaelblomer.factory_manager.infrastructure.model.RawMaterial;
import br.com.rafaelblomer.factory_manager.infrastructure.repositories.ProductRepository;
import br.com.rafaelblomer.factory_manager.infrastructure.repositories.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionOptimizerServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductionOptimizerService productionOptimizerService;

    private RawMaterial flour;
    private RawMaterial sugar;
    private RawMaterial eggs;

    @BeforeEach
    void setUp() {
        flour = new RawMaterial(1L, "RM001", "Wheat Flour", 1000.0, "g");
        sugar = new RawMaterial(2L, "RM002", "Sugar", 500.0, "g");
        eggs = new RawMaterial(3L, "RM003", "Eggs", 10.0, "un");
    }

    // -------------------------------------------------------------------------
    // calculateMaxProducible
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return correct max producible units when stock is sufficient")
    void calculateMaxProducible_shouldReturnCorrectQuantity() {
        Product cake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(
                        buildIngredient(flour, 500.0),
                        buildIngredient(sugar, 200.0),
                        buildIngredient(eggs, 3.0)
                ));

        Map<Long, Double> stock = new HashMap<>();
        stock.put(1L, 1000.0); // 2 cakes worth of flour
        stock.put(2L, 500.0);  // 2 cakes worth of sugar
        stock.put(3L, 10.0);   // 3 cakes worth of eggs

        int result = productionOptimizerService.calculateMaxProducible(cake, stock);

        assertEquals(2, result);
    }

    @Test
    @DisplayName("Should return 0 when stock is completely empty")
    void calculateMaxProducible_shouldReturnZeroWhenNoStock() {
        Product cake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(buildIngredient(flour, 500.0)));

        Map<Long, Double> stock = new HashMap<>();
        stock.put(1L, 0.0);

        int result = productionOptimizerService.calculateMaxProducible(cake, stock);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should return 0 when one ingredient is missing from stock")
    void calculateMaxProducible_shouldReturnZeroWhenIngredientMissing() {
        Product cake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(
                        buildIngredient(flour, 500.0),
                        buildIngredient(sugar, 200.0)
                ));

        Map<Long, Double> stock = new HashMap<>();
        stock.put(1L, 1000.0);
        // sugar is missing from stock map

        int result = productionOptimizerService.calculateMaxProducible(cake, stock);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should return 0 when stock is insufficient for even one unit")
    void calculateMaxProducible_shouldReturnZeroWhenStockInsufficient() {
        Product cake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(buildIngredient(flour, 500.0)));

        Map<Long, Double> stock = new HashMap<>();
        stock.put(1L, 499.0); // just below threshold

        int result = productionOptimizerService.calculateMaxProducible(cake, stock);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should be limited by the most restrictive ingredient")
    void calculateMaxProducible_shouldBeLimitedByMostRestrictiveIngredient() {
        Product cake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(
                        buildIngredient(flour, 500.0),  // allows 4 units
                        buildIngredient(sugar, 200.0),  // allows 2 units  <- bottleneck
                        buildIngredient(eggs, 1.0)      // allows 10 units
                ));

        Map<Long, Double> stock = new HashMap<>();
        stock.put(1L, 2000.0);
        stock.put(2L, 400.0);
        stock.put(3L, 10.0);

        int result = productionOptimizerService.calculateMaxProducible(cake, stock);

        assertEquals(2, result);
    }

    // -------------------------------------------------------------------------
    // optimize
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should return empty suggestion when no products are registered")
    void optimize_shouldReturnEmptyWhenNoProducts() {
        when(productRepository.findAll()).thenReturn(List.of());
        when(rawMaterialRepository.findAll()).thenReturn(List.of(flour, sugar, eggs));

        ProductionSuggestionResponseDTO result = productionOptimizerService.optimize();

        assertTrue(result.items().isEmpty());
        assertEquals(BigDecimal.ZERO, result.totalValue());
    }

    @Test
    @DisplayName("Should return empty suggestion when stock is zero")
    void optimize_shouldReturnEmptyWhenStockIsZero() {
        RawMaterial emptyFlour = new RawMaterial(1L, "RM001", "Wheat Flour", 0.0, "g");

        Product cake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(buildIngredient(emptyFlour, 500.0)));

        when(productRepository.findAll()).thenReturn(List.of(cake));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(emptyFlour));

        ProductionSuggestionResponseDTO result = productionOptimizerService.optimize();

        assertTrue(result.items().isEmpty());
        assertEquals(BigDecimal.ZERO, result.totalValue());
    }

    @Test
    @DisplayName("Should suggest the single producible product correctly")
    void optimize_shouldSuggestSingleProduct() {
        Product cake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(buildIngredient(flour, 500.0)));

        when(productRepository.findAll()).thenReturn(List.of(cake));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(flour));

        ProductionSuggestionResponseDTO result = productionOptimizerService.optimize();

        assertEquals(1, result.items().size());
        assertEquals("PR001", result.items().getFirst().productCode());
        assertEquals(2, result.items().getFirst().quantityToProduce());
        assertEquals(new BigDecimal("90.00"), result.totalValue());
    }

    @Test
    @DisplayName("Should prioritize the product with the highest total value when competing for the same resource")
    void optimize_shouldPrioritizeHighestValueProduct() {
        // Chocolate Cake: 500g flour, price R$45 -> 2 units = R$90
        // Plain Cake:     500g flour, price R$30 -> 2 units = R$60
        // Both compete for flour (1000g available)
        // Optimizer should pick Chocolate Cake first

        Product chocolateCake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(buildIngredient(flour, 500.0)));

        Product plainCake = buildProduct(2L, "PR002", "Plain Cake", new BigDecimal("30.00"),
                List.of(buildIngredient(flour, 500.0)));

        when(productRepository.findAll()).thenReturn(List.of(plainCake, chocolateCake));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(flour));

        ProductionSuggestionResponseDTO result = productionOptimizerService.optimize();

        assertEquals(1, result.items().size());
        assertEquals("PR001", result.items().getFirst().productCode());
        assertEquals(2, result.items().getFirst().quantityToProduce());
        assertEquals(new BigDecimal("90.00"), result.totalValue());
    }

    @Test
    @DisplayName("Should suggest multiple products when stock allows")
    void optimize_shouldSuggestMultipleProductsWhenStockAllows() {
        // Chocolate Cake uses flour + eggs
        // Butter Cookies uses only sugar
        // No competition -> both should be produced

        Product chocolateCake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(
                        buildIngredient(flour, 500.0),
                        buildIngredient(eggs, 3.0)
                ));

        Product butterCookies = buildProduct(2L, "PR002", "Butter Cookies", new BigDecimal("20.00"),
                List.of(buildIngredient(sugar, 200.0)));

        when(productRepository.findAll()).thenReturn(List.of(chocolateCake, butterCookies));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(flour, sugar, eggs));

        ProductionSuggestionResponseDTO result = productionOptimizerService.optimize();

        assertEquals(2, result.items().size());

        var cakeItem = result.items().stream()
                .filter(i -> i.productCode().equals("PR001"))
                .findFirst().orElseThrow();

        var cookiesItem = result.items().stream()
                .filter(i -> i.productCode().equals("PR002"))
                .findFirst().orElseThrow();

        assertEquals(2, cakeItem.quantityToProduce());
        assertEquals(2, cookiesItem.quantityToProduce());
        assertEquals(new BigDecimal("130.00"), result.totalValue());
    }

    @Test
    @DisplayName("Should calculate total value correctly")
    void optimize_shouldCalculateTotalValueCorrectly() {
        Product cake = buildProduct(1L, "PR001", "Chocolate Cake", new BigDecimal("45.00"),
                List.of(buildIngredient(flour, 500.0)));

        when(productRepository.findAll()).thenReturn(List.of(cake));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(flour));

        ProductionSuggestionResponseDTO result = productionOptimizerService.optimize();

        BigDecimal expectedTotal = new BigDecimal("45.00").multiply(BigDecimal.valueOf(2));
        assertEquals(expectedTotal, result.totalValue());
    }

    // -------------------------------------------------------------------------
    // Builders
    // -------------------------------------------------------------------------

    private Product buildProduct(Long id, String code, String name, BigDecimal price,
                                 List<ProductIngredient> ingredients) {
        Product product = new Product();
        product.setId(id);
        product.setCode(code);
        product.setName(name);
        product.setPrice(price);
        product.setIngredients(ingredients);
        return product;
    }

    private ProductIngredient buildIngredient(RawMaterial rawMaterial, Double quantityRequired) {
        ProductIngredient ingredient = new ProductIngredient();
        ingredient.setRawMaterial(rawMaterial);
        ingredient.setQuantityRequired(quantityRequired);
        return ingredient;
    }
}
