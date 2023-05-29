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
        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setId(1L);
        restaurantModel.setNit("11010101L");
        restaurantModel.setName("Giorno's pizza");
        restaurantModel.setPhone("12323243");
        restaurantModel.setAddress("Home#4");
        restaurantModel.setUrlLogo("asdfsdf.com/img.png");

        return restaurantModel;
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

    public static DishModel obtainDish(CategoryDishModel categoryModel, RestaurantModel restaurantModel) {
        DishModel dishModel = new DishModel();

        dishModel.setName("Pizza");
        dishModel.setCategory(categoryModel);
        dishModel.setPrice(60000L);
        dishModel.setRestaurant(restaurantModel);

        return dishModel;
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

    public static CategoryDishModel getCategoryModel() {
        CategoryDishModel categoryModel = new CategoryDishModel();
        categoryModel.setId(1L);
        categoryModel.setName("Italian");
        categoryModel.setDescription("Italian food");
        return categoryModel;
    }
}
