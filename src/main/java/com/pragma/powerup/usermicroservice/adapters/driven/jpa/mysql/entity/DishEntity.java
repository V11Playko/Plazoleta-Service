package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;

@Entity
@Table(name = "dish")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "dish_id", nullable = false)
    private Long id;
    private String name;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private CategoryEntity category;
    private String description;
    private Long price;
    @ManyToOne
    @JoinColumn(name = "idRestaurant", nullable = false)
    private RestaurantEntity restaurant;
    private String urlImage;
    private boolean active;
}
