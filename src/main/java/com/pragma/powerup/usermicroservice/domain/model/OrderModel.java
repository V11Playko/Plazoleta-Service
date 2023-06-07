package com.pragma.powerup.usermicroservice.domain.model;

import java.time.LocalDateTime;

public class OrderModel {
    private Long id;
    private String idClient;
    private LocalDateTime date;
    private String state;
    private String securityPin;
    private RestaurantEmployeeModel emailChef;
    private RestaurantModel restaurant;

    public OrderModel(Long id, String idClient, LocalDateTime date, String state, String securityPin, RestaurantEmployeeModel emailChef, RestaurantModel restaurant) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.state = state;
        this.securityPin = securityPin;
        this.emailChef = emailChef;
        this.restaurant = restaurant;
    }

    public OrderModel() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSecurityPin() {
        return securityPin;
    }

    public void setSecurityPin(String securityPin) {
        this.securityPin = securityPin;
    }

    public RestaurantEmployeeModel getEmailChef() {
        return emailChef;
    }

    public void setEmailChef(RestaurantEmployeeModel emailChef) {
        this.emailChef = emailChef;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
    }
}
