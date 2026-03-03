package br.com.rafaelblomer.factory_manager.infrastructure.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "raw_material")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String code;

    @NotBlank
    private String name;

    @NotNull
    @Min(0)
    @Column(name = "stock_quantity")
    private Double stockQuantity;

    @NotBlank
    private String unit;

    public RawMaterial() {
    }

    public RawMaterial(Long id, String code, String name, Double stockQuantity, String unit) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Double stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
