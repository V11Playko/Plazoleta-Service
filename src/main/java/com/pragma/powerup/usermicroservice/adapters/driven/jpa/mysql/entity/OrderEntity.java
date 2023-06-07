package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;
    @Column(name="client_id")
    private Long idClient;
    @Column(name="date_order")
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    @Column(name="state")
    private OrderStateType state;
    @Column(name = "pin_seguridad")
    private String securityPin;
    @ManyToOne
    @JoinColumn(name = "email_chef", referencedColumnName = "email_persona")
    private RestaurantEmployeeEntity emailChef;
    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private RestaurantEntity restaurant;
    @OneToMany(mappedBy="order")
    private List<OrdersDishesEntity> orderDish;
}
