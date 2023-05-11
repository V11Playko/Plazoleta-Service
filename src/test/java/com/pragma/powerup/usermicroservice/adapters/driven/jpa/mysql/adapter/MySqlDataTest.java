package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;

public class MySqlDataTest {

    public static RestaurantEntity obtainRestaurantEntity() {
        return new RestaurantEntity(
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
