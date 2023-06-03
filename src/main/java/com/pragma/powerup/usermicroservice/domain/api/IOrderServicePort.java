package com.pragma.powerup.usermicroservice.domain.api;

import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;

import java.util.List;

public interface IOrderServicePort {
    void newOrder(String idRestaurant,String idClient, List<OrdersDishesModel> ordersDishesModels);
    List<OrderWithDishesModel> listOrdersByState(String orderState, int page, int elementsXpage, String employeeEmail);
}
