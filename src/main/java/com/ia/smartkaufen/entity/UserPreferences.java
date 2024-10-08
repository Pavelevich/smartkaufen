package com.ia.smartkaufen.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserPreferences {
    @Id
    private int userID;

    private boolean prefersOrganic;
    private boolean glutenFree;
    private boolean lactoseFree;
    private boolean vegan;

    // Getters and Setters
}
