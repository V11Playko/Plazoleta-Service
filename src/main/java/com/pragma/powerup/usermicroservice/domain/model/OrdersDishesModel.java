package com.pragma.powerup.usermicroservice.domain.model;

public class OrdersDishesModel {
    private OrderModel order;
    private DishModel dish;
    private int amount;

    public OrdersDishesModel(OrderModel order, DishModel dish, int amount) {
        this.order = order;
        this.dish = dish;
        this.amount = amount;
    }

    public OrdersDishesModel(){

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
