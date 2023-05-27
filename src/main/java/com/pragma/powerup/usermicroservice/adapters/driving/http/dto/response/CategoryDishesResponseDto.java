package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CategoryDishesResponseDto {
    private Long id;
    private String name;
    private String description;
    private List<GetDishResponseDto> dishes;
}
