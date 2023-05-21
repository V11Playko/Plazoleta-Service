package com.pragma.powerup.usermicroservice.domain.model;

public class OrdersDishesModel {
    private OrderModel order;
    private DishModel dish;
    private String amount;

    public OrdersDishesModel(OrderModel order, DishModel dish, String amount) {
        this.order = order;
        this.dish = dish;
        this.amount = amount;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public DishModel getDish() {
        return dish;
    }

    public void setDish(DishModel dish) {
        this.dish = dish;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
