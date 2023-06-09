package com.pragma.powerup.usermicroservice.domain.api;

import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;

import java.util.List;

public interface IOrderServicePort {
    OrderModel getOrder(Long id);
    void newOrder(String idRestaurant,String idClient, List<OrdersDishesModel> ordersDishesModels);
    List<OrderWithDishesModel> listOrdersByState(String orderState, int page, int elementsXpage, String employeeEmail);
    OrderWithDishesModel assignOrder(String employeeEmail, Long orderId);
    OrderModel changeOrderToReady(String employeeEmail, Long orderId);
    OrderModel changeOrderToDelivered(String employeeEmail, Long orderId, String securityCode);
    void cancelOrder(String clientEmail, Long orderId);
    void cancelOrderByWaitingTime(int timeLimit);
}
