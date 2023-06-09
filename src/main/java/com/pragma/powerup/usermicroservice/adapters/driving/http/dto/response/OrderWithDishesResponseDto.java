package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderWithDishesResponseDto {

    private Long id;
    private String client;
    private String date;
    private String state;
    private String chef;
    private String restaurant;
    private List<DishResponseDto> orderDishes;
}
