package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers;


import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;

public interface IDishHandler {
    void saveDish(DishRequestDto dishRequestDto);
}
