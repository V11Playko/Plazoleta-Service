package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

public class DomainData {

    public static DishModel obtainDish(CategoryDishModel categoryModel, RestaurantModel restaurantModel) {
        DishModel dishModel = new DishModel();

        dishModel.setName("Pizza");
        dishModel.setCategory(categoryModel);
        dishModel.setPrice(60000L);
        dishModel.setRestaurant(restaurantModel);

        return dishModel;
    }

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

    public static CategoryDishModel getCategoryModel() {
        CategoryDishModel categoryModel = new CategoryDishModel();
        categoryModel.setId(1L);
        categoryModel.setName("Italian");
        categoryModel.setDescription("Italian food");
        return categoryModel;
    }
}
