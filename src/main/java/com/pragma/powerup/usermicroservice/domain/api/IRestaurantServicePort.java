package com.pragma.powerup.usermicroservice.domain.api;

import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(RestaurantModel restaurantModel);
    RestaurantModel getRestaurant(Long id);
    List<RestaurantModel> listRestaurant(int page, int numberOfElements);
    void deleteRestaurant(Long id);
}
