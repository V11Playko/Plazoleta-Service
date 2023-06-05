package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "restaurant-employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantEmployeeEntity {
    @Id
    @Column(name = "email_persona", nullable = false)
    private String userEmail;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_restaurante", nullable = false)
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "emailChef")
    private List<OrderEntity> order;
}
