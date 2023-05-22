package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers;


import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.DishResponseDto;

public interface IOwnerHandler {
    void saveDish(DishRequestDto dishRequestDto, String id_owner);
    DishResponseDto getDish(Long id);
    void updateDish(DishUpdateRequest dishUpdateRequest,String id_owner);
}
