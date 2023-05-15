package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request;

import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DishRequestDto {
    private Long idDish;
    private String name;
    private String category;
    private String description;
    private String price;
    private String restaurant;
    private String urlImage;
    private Boolean state;
}
