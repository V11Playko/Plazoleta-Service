package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.UpdateDishStateRequestDto;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

public class HttpData {

    public static RestaurantModel obtainRestaurant() {
        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setId(1L);
        restaurantModel.setNit("11010101L");
        restaurantModel.setName("Giorno's pizza");
        restaurantModel.setPhone("12323243");
        restaurantModel.setAddress("Home#4");
        restaurantModel.setUrlLogo("asdfsdf.com/img.png");

        return restaurantModel;
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

    public static DishModel obtainDish(CategoryDishModel categoryModel, RestaurantModel restaurantModel) {
        DishModel dishModel = new DishModel();

        dishModel.setName("Pizza");
        dishModel.setCategory(categoryModel);
        dishModel.setPrice(60000L);
        dishModel.setRestaurant(restaurantModel);

        return dishModel;
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

    public static DishUpdateRequest obtainDishUpdate() {
        return new DishUpdateRequest(
                1L,
                "Memorable",
                4500L
        );
    }

    public static UpdateDishStateRequestDto obtainDishState() {
        return new UpdateDishStateRequestDto(
                1L,
                false
        );
    }

    public static CategoryDishModel getCategoryModel() {
        CategoryDishModel categoryModel = new CategoryDishModel();
        categoryModel.setId(1L);
        categoryModel.setName("Italian");
        categoryModel.setDescription("Italian food");
        return categoryModel;
    }
}
