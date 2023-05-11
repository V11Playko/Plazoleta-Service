package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
}
