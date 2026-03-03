package br.com.rafaelblomer.factory_manager.business.dtos;

import java.math.BigDecimal;

public record ProductionItemResponseDTO(
        String productCode,
        String productName,
        BigDecimal unitPrice,
        int quantityToProduce,
        BigDecimal totalItemValue
) {}
