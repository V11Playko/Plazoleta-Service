package com.pragma.powerup.usermicroservice.domain.api;


import com.pragma.powerup.usermicroservice.domain.model.OrderModel;

public interface IOrderServicePort {
    void saveOrder(OrderModel orderModel);
}
