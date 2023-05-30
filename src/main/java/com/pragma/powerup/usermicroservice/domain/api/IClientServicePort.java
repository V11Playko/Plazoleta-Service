package com.pragma.powerup.usermicroservice.domain.api;

import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

import java.util.List;

public interface IClientServicePort {
    List<RestaurantModel> listRestaurant(int page, int numberOfElements);
    List<CategoryWithDishesModel> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage);
    void newOrder(String idRestaurant,String idClient, List<OrdersDishesModel> ordersDishesModels);
}
