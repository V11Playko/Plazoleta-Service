package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.OrderDishRequestDto;

import java.util.List;

public interface IOrderHandler {
    void newOrder(String idRestaurant,String idClient, List<OrderDishRequestDto> ordersDishesModels);
}
