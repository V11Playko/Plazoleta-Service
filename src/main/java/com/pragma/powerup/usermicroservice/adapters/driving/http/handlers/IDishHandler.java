package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers;


import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.CreateEmployeeRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.UpdateDishStateRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryAveragePriceResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryDishesResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.RestaurantEmployeeResponseDto;

import java.util.List;

public interface IDishHandler {
    void saveDish(DishRequestDto dishRequestDto, String idOwner);
    DishResponseDto getDish(Long id);
    void updateDish(DishUpdateRequest dishUpdateRequest,String idOwner);
    void updateState(UpdateDishStateRequestDto updateDishStateRequestDto, String idOwner);
    RestaurantEmployeeResponseDto createEmployee(CreateEmployeeRequestDto createEmployeeRequestDto, String idRestaurant, String emailEmployee);
    List<CategoryDishesResponseDto> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage);
    List<CategoryAveragePriceResponseDto> calculateAverageByCategory(String idOwner);

}
