package com.pragma.powerup.usermicroservice.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrderWithDishesModel {
    private Long id;
    private LocalDateTime date;
    private String client;
    private String state;
    private String chef;
    private List<OrdersDishesModel> orderDishes;

    public OrderWithDishesModel(Long id, LocalDateTime date, String client, String state, String chef, List<OrdersDishesModel> orderDishes) {
        this.id = id;
        this.date = date;
        this.client = client;
        this.state = state;
        this.chef = chef;
        this.orderDishes = orderDishes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public List<OrdersDishesModel> getOrderDishes() {
        return orderDishes;
    }

    public void setOrderDishes(List<OrdersDishesModel> orderDishes) {
        this.orderDishes = orderDishes;
    }
}
