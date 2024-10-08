package com.ia.smartkaufen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    private ProductCategory category;

    @Column(nullable = false)
    private BigDecimal price;

    private int stock;

    private LocalDateTime expirationDate;

    private BigDecimal weight;

    private BigDecimal volume;

    @Column(nullable = false, unique = true)
    private String upc;  // Barcode

    private String description;

    private String allergens;

    private boolean organic;

    private boolean glutenFree;

    private LocalDateTime dateAdded;

    private LocalDateTime lastUpdated;

    @OneToOne(mappedBy = "product")
    private Nutrition nutrition;

    // Getters and Setters
}