package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.OrderDishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderResponseDto;

import java.util.List;

public interface IOrderHandler {
    void newOrder(String idRestaurant,String idClient, List<OrderDishRequestDto> ordersDishesModels);
    List<OrderResponseDto> listOrdersByState(String orderState, int page, int elementsPerPage, String employeeEmail);
}
