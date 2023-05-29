package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.ListRestaurantForClientResponseDto;

import java.util.List;

public interface IClientHandler {
    List<ListRestaurantForClientResponseDto> listRestaurant(int page, int numberOfElements);
}
