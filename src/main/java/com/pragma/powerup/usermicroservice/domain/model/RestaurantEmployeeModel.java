package com.pragma.powerup.usermicroservice.domain.model;

public class RestaurantEmployeeModel {
    private String userEmail;
    private RestaurantModel restaurant;

    public RestaurantEmployeeModel(String userEmail, RestaurantModel restaurant) {
        this.userEmail = userEmail;
        this.restaurant = restaurant;
    }

    public RestaurantEmployeeModel() {

    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
    }
}
