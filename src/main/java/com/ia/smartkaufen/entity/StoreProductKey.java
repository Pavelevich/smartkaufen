package com.ia.smartkaufen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class StoreProductKey implements Serializable {

    private int productID;
    private int storeID;

    // Constructors, Getters, Setters, hashCode and equals methods
}