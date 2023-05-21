package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
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
    public static DishEntity obtainDishEntity() {
        DishEntity dish = new DishEntity();

        dish.setId(1L);
        dish.setName("Burguer");
        dish.setCategory(new CategoryEntity());
        dish.setDescription("Rico");
        dish.setPrice(5000L);
        dish.setRestaurant(new RestaurantEntity());
        dish.setUrlImage("https://burguer.com");

        return dish;
    }
}
