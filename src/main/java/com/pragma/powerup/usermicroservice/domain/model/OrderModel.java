package com.pragma.powerup.usermicroservice.domain.model;

public class OrderModel {
    private OrdersDishesModel idOrder;
    private String idClient;
    private String date;
    private String state;
    private String idChef;
    private RestaurantModel restaurant;

    public OrderModel(OrdersDishesModel idOrder, String idClient, String date, String state, String idChef, RestaurantModel restaurant) {
        this.idOrder = idOrder;
        this.idClient = idClient;
        this.date = date;
        this.state = state;
        this.idChef = idChef;
        this.restaurant = restaurant;
    }

    public OrdersDishesModel getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(OrdersDishesModel idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdChef() {
        return idChef;
    }

    public void setIdChef(String idChef) {
        this.idChef = idChef;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
    }
}
