package com.ia.smartkaufen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class ShoppingListProduct {
    @EmbeddedId
    private ShoppingListProductKey id;

    @ManyToOne
    @MapsId("listID")
    @JoinColumn(name = "listID")
    private ShoppingList shoppingList;

    @ManyToOne
    @MapsId("productID")
    @JoinColumn(name = "productID")
    private Product product;

    private int quantity;
    private BigDecimal subtotal;

    // Getters and Setters
}