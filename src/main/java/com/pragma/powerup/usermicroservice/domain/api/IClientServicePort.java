package com.pragma.powerup.usermicroservice.domain.api;

import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

import java.util.List;

public interface IClientServicePort {
    List<RestaurantModel> listRestaurant(int page, int numberOfElements);

    List<CategoryWithDishesModel> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage);
}
