package com.pragma.powerup.usermicroservice.domain.model;

public class DishModel {
    private OrdersDishesModel idDish;
    private String name;
    private CategoryDishModel category;
    private String description;
    private Long price;
    private RestaurantModel restaurant;
    private String urlImage;
    private boolean state;

    public DishModel(OrdersDishesModel idDish, String name, CategoryDishModel category, String description, Long price, RestaurantModel restaurant, String urlImage, Boolean state) {
        this.idDish = idDish;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
        this.urlImage = urlImage;
        this.state = state;
    }

    public OrdersDishesModel getIdDish() {
        return idDish;
    }

    public void setIdDish(OrdersDishesModel idDish) {
        this.idDish = idDish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryDishModel getCategory() {
        return category;
    }

    public void setCategory(CategoryDishModel category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
