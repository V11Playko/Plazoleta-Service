package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

public class DomainData {

    public static DishModel obtainDish() {
        return new DishModel(
                1L,
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

    public static RestaurantModel obtainRestaurant() {
        return new RestaurantModel(
                1L,
                "Mons",
                "Cll addams",
                "34545",
                "+57 3208265245",
                "https://example.com/logos.png",
                "546465456"
        );
    }
}
