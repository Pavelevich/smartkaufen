package com.ia.smartkaufen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int promotionID;

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

    private BigDecimal discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    // Getters and Setters
}