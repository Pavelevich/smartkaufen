package com.ia.smartkaufen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class ShoppingListProductKey implements Serializable {

    private int listID;
    private int productID;

    // Constructors, Getters, Setters, hashCode and equals methods
}