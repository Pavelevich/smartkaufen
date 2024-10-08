package com.ia.smartkaufen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class StoreProduct {
    @EmbeddedId
    private StoreProductKey id;

    @ManyToOne
    @MapsId("productID")
    @JoinColumn(name = "productID")
    private Product product;

    @ManyToOne
    @MapsId("storeID")
    @JoinColumn(name = "storeID")
    private Store store;

    private BigDecimal price;
    private int stock;

    // Getters and Setters
}