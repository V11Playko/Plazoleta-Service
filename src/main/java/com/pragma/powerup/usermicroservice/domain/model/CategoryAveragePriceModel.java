package com.pragma.powerup.usermicroservice.domain.model;

public class CategoryAveragePriceModel {
    private Long id;
    private String name;
    private String averagePerDish;

    public CategoryAveragePriceModel(Long id, String name, String averagePerDish) {
        this.id = id;
        this.name = name;
        this.averagePerDish = averagePerDish;
    }

    public CategoryAveragePriceModel() {

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

    public String getAveragePerDish() {
        return averagePerDish;
    }

    public void setAveragePerDish(String averagePerDish) {
        this.averagePerDish = averagePerDish;
    }
}
