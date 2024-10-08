package com.ia.smartkaufen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Nutrition {
    @Id
    @OneToOne
    @JoinColumn(name = "productID")
    private Product product;

    private double energy;
    private double protein;
    private double totalFat;
    private double carbohydrates;
    private double fiber;
    private double sugar;
    private double sodium;

    // Getters and Setters
}