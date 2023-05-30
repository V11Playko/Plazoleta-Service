package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.OrderDishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryDishesResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.ListRestaurantForClientResponseDto;

import java.util.List;

public interface IClientHandler {
    List<ListRestaurantForClientResponseDto> listRestaurant(int page, int numberOfElements);
    List<CategoryDishesResponseDto> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage);
    void newOrder(String idRestaurant,String idClient, List<OrderDishRequestDto> ordersDishesModels);
}
