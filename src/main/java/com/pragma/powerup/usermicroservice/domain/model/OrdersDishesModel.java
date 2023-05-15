package com.pragma.powerup.usermicroservice.domain.model;

public class OrdersDishesModel {
    private Long idOrder;
    private Long idDish;
    private String amount;

    public OrdersDishesModel(Long idOrder, Long idDish, String amount) {
        this.idOrder = idOrder;
        this.idDish = idDish;
        this.amount = amount;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdDish() {
        return idDish;
    }

    public void setIdDish(Long idDish) {
        this.idDish = idDish;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
