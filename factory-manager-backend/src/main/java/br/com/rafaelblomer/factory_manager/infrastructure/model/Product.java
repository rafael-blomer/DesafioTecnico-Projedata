package br.com.rafaelblomer.factory_manager.infrastructure.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String code;

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductIngredient> ingredients = new ArrayList<>();

    public Product() {
    }

    public Product(Long id, String code, String name, BigDecimal price, List<ProductIngredient> ingredients) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<ProductIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ProductIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
