package com.pragma.powerup.usermicroservice.domain.model;

import java.util.List;

public class CategoryWithDishesModel {
    private Long id;
    private String name;
    private String description;
    private List<DishModel> dishes;

    public CategoryWithDishesModel(Long id, String name, String description, List<DishModel> dishes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dishes = dishes;
    }

    public CategoryWithDishesModel() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DishModel> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishModel> dishes) {
        this.dishes = dishes;
    }
}
