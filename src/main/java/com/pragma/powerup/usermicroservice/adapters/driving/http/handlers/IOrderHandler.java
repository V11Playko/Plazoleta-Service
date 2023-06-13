package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.OrderDishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.AssignOrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderWithDishesResponseDto;

import java.util.List;

public interface IOrderHandler {
    OrderResponseDto getOrder(Long id);
    void newOrder(String idRestaurant,String idClient, List<OrderDishRequestDto> ordersDishesModels);
    List<OrderWithDishesResponseDto> listOrdersByState(String orderState, int page, int elementsPerPage, String employeeEmail);
    List<AssignOrderResponseDto> assignOrdersToEmployee(List<Long> ordersId, String employeeEmail);
    void changeOrderToReady(Long orderId, String employeeEmail);
    void changeOrderToDelivered(Long orderId, String employeeEmail, String securityCode);
    void cancelOrder(Long orderId, String clientEmail);
}
