package com.pragma.powerup.usermicroservice.domain.api;

import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

public interface IAdminServicePort {
    void saveRestaurant(RestaurantModel restaurantModel);
    RestaurantModel getRestaurant(Long id);

}
