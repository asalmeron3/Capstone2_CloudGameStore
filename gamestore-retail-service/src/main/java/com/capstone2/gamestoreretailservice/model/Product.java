package com.capstone2.gamestoreretailservice.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private int id;

    @NotNull(message = "'name' must be specified for this product")
    @Size(max = 11, message = "'name' cannot excced 11 characters")
    private String name;

    @NotNull(message = "'description' must be specified for this product")
    @Size(max = 50, message = "'description' cannot exceed 50 characters")
    private String description;

    @NotNull(message = "'listPrice' must be specified for this product")
    @Positive(message = "'listPrice' MUST be greater than or equal to 0.00")
    @Digits(integer = 7, fraction = 2, message = "'listPrice' must be an number less than or equal to 9999999.99")
    private BigDecimal listPrice;

    @NotNull(message = "'unitCost'  must be specified for this product")
    @Positive(message = "'unitCost' MUST be greater than or equal to 0.00")
    @Digits(integer = 7, fraction = 2, message = "'unitCost' must be an number less than or equal to 9999999.99")
    private BigDecimal unitCost;

    public Product() {}

    public Product(String name, String description, BigDecimal listPrice, BigDecimal unitCost) {
        setName(name);
        setDescription(description);
        setListPrice(listPrice);
        setUnitCost(unitCost);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        listPrice = listPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.listPrice = listPrice;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        unitCost = unitCost.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.unitCost = unitCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(listPrice, product.listPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, listPrice);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", listPrice=" + listPrice +
                ", unitCost=" + unitCost +
                '}';
    }
}
