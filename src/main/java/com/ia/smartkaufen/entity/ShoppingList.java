package com.ia.smartkaufen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int listID;

    private int userID;
    private LocalDateTime dateCreated;
    private BigDecimal totalAmount;
    private String ShoppingListData;

    @OneToMany(mappedBy = "shoppingList")
    private List<ShoppingListProduct> products;

    // Getters and Setters
}