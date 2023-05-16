package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

public class HttpData {
    public static RestaurantModel obtainRestaurant() {
        return new RestaurantModel(
                1L,
                "Mons",
                "Cll addams",
                "3205",
                "+57 3208265245",
                "https://example.com/logos.png",
                "546465456"
        );
    }

    public static RestaurantRequestDto obtainRestaurantRequest() {
        return new RestaurantRequestDto(
                "Mons",
                "Cll addams",
                "3205",
                "+57 3208265245",
                "https://example.com/logos.png",
                "546465456"
        );
    }

    public static DishModel obtainDish() {
        return new DishModel(
                new OrdersDishesModel(1L, 2L, "50"),
                "Burguer",
                new CategoryDishModel(1L, "Comida rapida", "rico"),
                "Deliciosa",
                5000L,
                new RestaurantModel(1L, "Aja", "cll 4 lemuer",
                        "2", "+57 3208265245","https://example.com/logos.png",
                        "5464654564251"),
                "https://burguer.com",
                true
        );
    }

    public static DishRequestDto obtainDishRequest() {
        return new DishRequestDto(
                1L,
                "Burguer",
                "1",
                "Deliciosa",
                5000L,
                "2",
                "https://burguer.com"
        );
    }
}
