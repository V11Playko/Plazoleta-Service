package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

public class MySqlData {

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

    public static RestaurantEntity obtainRestaurantEntity() {
        RestaurantEntity restaurantEntity = new RestaurantEntity();

        restaurantEntity.setId(1L);
        restaurantEntity.setName("Angus Hamburguers");
        restaurantEntity.setAddress("Street 25");
        restaurantEntity.setIdOwner("2");
        restaurantEntity.setPhone("3013218520");
        restaurantEntity.setUrlLogo("www.logo.es");
        restaurantEntity.setNit("31534564");

        return restaurantEntity;
    }
}
