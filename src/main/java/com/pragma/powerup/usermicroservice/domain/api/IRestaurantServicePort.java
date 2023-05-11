package com.pragma.powerup.usermicroservice.domain.api;

import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

public interface IRestaurantServicePort {
    void saveRestaurant(RestaurantModel restaurantModel);

}
