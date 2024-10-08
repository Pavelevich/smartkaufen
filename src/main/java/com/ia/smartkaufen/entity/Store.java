package com.ia.smartkaufen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeID;

    private String name;

    private String location;

    private String openingTime;

    private String closingTime;

    @OneToMany(mappedBy = "store")
    private List<StoreProduct> storeProducts;

    // Getters and Setters
}