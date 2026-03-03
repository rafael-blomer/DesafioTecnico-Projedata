package br.com.rafaelblomer.factory_manager.infrastructure.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product_ingredient")
public class ProductIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "raw_material_id")
    private RawMaterial rawMaterial;

    @NotNull
    @Min(0)
    @Column(name = "quantity_required")
    private Double quantityRequired;

    public ProductIngredient() {
    }

    public ProductIngredient(Long id, Product product, RawMaterial rawMaterial, Double quantityRequired) {
        this.id = id;
        this.product = product;
        this.rawMaterial = rawMaterial;
        this.quantityRequired = quantityRequired;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public Double getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(Double quantityRequired) {
        this.quantityRequired = quantityRequired;
    }
}
