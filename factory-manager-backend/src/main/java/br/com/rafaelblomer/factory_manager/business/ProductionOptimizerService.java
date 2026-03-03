package br.com.rafaelblomer.factory_manager.business;

import br.com.rafaelblomer.factory_manager.business.dtos.ProductionItemResponseDTO;
import br.com.rafaelblomer.factory_manager.business.dtos.ProductionSuggestionResponseDTO;
import br.com.rafaelblomer.factory_manager.infrastructure.model.Product;
import br.com.rafaelblomer.factory_manager.infrastructure.repositories.ProductRepository;
import br.com.rafaelblomer.factory_manager.infrastructure.repositories.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductionOptimizerService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    /**
     * Analyzes the current stock and suggests which products to produce
     * in order to maximize the total sale value.
     *
     * @return ProductionSuggestionResponseDTO with items to produce and total value
     * Flow:
     * - Loads all products and builds a mutable stock map
     * - Calculates how many units of each product can be produced
     * - Sorts products by total potential value (units * price) descending
     * - Greedily allocates stock starting from highest-value product
     * - Returns the suggestion with total value
     */
    @Transactional(readOnly = true)
    public ProductionSuggestionResponseDTO optimize() {
        List<Product> products = productRepository.findAll();
        Map<Long, Double> availableStock = buildStockMap();

        List<Product> sortedProducts = sortByPotentialValue(products, availableStock);

        List<ProductionItemResponseDTO> items = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : sortedProducts) {
            int quantity = calculateMaxProducible(product, availableStock);
            if (quantity > 0) {
                deductFromStock(product, availableStock, quantity);
                BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                items.add(new ProductionItemResponseDTO(
                        product.getCode(),
                        product.getName(),
                        product.getPrice(),
                        quantity,
                        itemTotal
                ));
                totalValue = totalValue.add(itemTotal);
            }
        }

        return new ProductionSuggestionResponseDTO(items, totalValue);
    }

    // UTILITY METHODS

    /**
     * Builds a mutable map of available stock keyed by RawMaterial ID.
     *
     * @return Map of rawMaterialId -> available quantity
     */
    private Map<Long, Double> buildStockMap() {
        Map<Long, Double> stockMap = new HashMap<>();
        rawMaterialRepository.findAll()
                .forEach(rm -> stockMap.put(rm.getId(), rm.getStockQuantity()));
        return stockMap;
    }

    /**
     * Sorts products by their total potential value in descending order.
     * Total potential value = max producible units * unit price
     * This ensures higher-value products get stock priority.
     *
     * @param products       List of all products
     * @param availableStock Current stock map
     * @return Sorted list of products
     */
    private List<Product> sortByPotentialValue(List<Product> products, Map<Long, Double> availableStock) {
        return products.stream()
                .sorted(Comparator.comparing(product -> {
                    int maxUnits = calculateMaxProducible(product, availableStock);
                    return product.getPrice().multiply(BigDecimal.valueOf(maxUnits));
                }, Comparator.reverseOrder()))
                .toList();
    }

    /**
     * Calculates the maximum number of units of a product that can be
     * produced given the current available stock.
     *
     * @param product        Product to evaluate
     * @param availableStock Current stock map
     * @return Maximum producible quantity (0 if any ingredient is missing)
     */
    public int calculateMaxProducible(Product product, Map<Long, Double> availableStock) {
        int maxUnits = Integer.MAX_VALUE;
        for (var ingredient : product.getIngredients()) {
            Long rawMaterialId = ingredient.getRawMaterial().getId();
            Double stock = availableStock.getOrDefault(rawMaterialId, 0.0);
            int possibleUnits = (int) Math.floor(stock / ingredient.getQuantityRequired());
            maxUnits = Math.min(maxUnits, possibleUnits);
        }
        return maxUnits == Integer.MAX_VALUE ? 0 : maxUnits;
    }

    /**
     * Deducts the consumed raw materials from the available stock
     * after deciding to produce a given quantity of a product.
     *
     * @param product        Product being produced
     * @param availableStock Mutable stock map to update
     * @param quantity       Number of units to produce
     */
    private void deductFromStock(Product product, Map<Long, Double> availableStock, int quantity) {
        product.getIngredients().forEach(ingredient -> {
            Long rawMaterialId = ingredient.getRawMaterial().getId();
            double consumed = ingredient.getQuantityRequired() * quantity;
            availableStock.merge(rawMaterialId, consumed, (current, toDeduct) -> current - toDeduct);
        });
    }
}
