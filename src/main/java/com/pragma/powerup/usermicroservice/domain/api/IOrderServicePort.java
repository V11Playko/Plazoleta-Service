package com.pragma.powerup.usermicroservice.domain.api;

import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;

import java.util.List;

public interface IOrderServicePort {
    void newOrder(String idRestaurant,String idClient, List<OrdersDishesModel> ordersDishesModels);
}
