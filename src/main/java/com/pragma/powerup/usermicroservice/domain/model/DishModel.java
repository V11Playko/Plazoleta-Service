package com.pragma.powerup.usermicroservice.domain.model;

public class DishModel {
    private Long id;
    private String name;
    private CategoryDishModel category;
    private String description;
    private Long price;
    private RestaurantModel restaurant;
    private String urlImage;
    private boolean active;

    public DishModel(Long id, String name, CategoryDishModel category, String description, Long price, RestaurantModel restaurant, String urlImage, boolean active) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
        this.urlImage = urlImage;
        this.active = active;
    }

    public DishModel() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean state) {
        this.active = state;
    }
}
