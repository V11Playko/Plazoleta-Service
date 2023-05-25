package com.pragma.powerup.usermicroservice.domain.api;

import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

import java.util.List;

public interface IClientServicePort {
    List<RestaurantModel> listRestaurant(int page, int numberOfElements);

//    List<DishModel> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage);
}
