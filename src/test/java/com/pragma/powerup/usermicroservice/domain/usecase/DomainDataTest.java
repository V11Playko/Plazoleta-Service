package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

public class DomainDataTest {

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
}
