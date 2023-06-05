package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

import java.time.LocalDateTime;

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
        restaurantModel.setIdOwner("2");

        return restaurantModel;
    }

    public static CategoryDishModel getCategoryModel() {
        CategoryDishModel categoryModel = new CategoryDishModel();
        categoryModel.setId(1L);
        categoryModel.setName("Italian");
        categoryModel.setDescription("Italian food");
        return categoryModel;
    }

    public static OrdersDishesModel getOrderDishModelWithDish(DishModel dishModel) {
        OrdersDishesModel orderDishModel = new OrdersDishesModel();
        orderDishModel.setDish(dishModel);
        orderDishModel.setAmount(2);
        return orderDishModel;
    }

    public static OrderModel getOrderModel() {
        OrderModel orderModel = new OrderModel();
        orderModel.setDate(LocalDateTime.now());
        return orderModel;
    }

    public static RestaurantEmployeeModel getRestaurantEmployee(RestaurantModel restaurantModel) {
        RestaurantEmployeeModel restaurantEmployeeModel = new RestaurantEmployeeModel();
        restaurantEmployeeModel.setUserEmail("employee@mail.com");
        restaurantEmployeeModel.setRestaurant(restaurantModel);
        return restaurantEmployeeModel;
    }

    public static OrdersDishesModel getOrderDishModel(DishModel dishModel, OrderModel orderModel, int amount) {
        OrdersDishesModel orderDishModel = new OrdersDishesModel();
        orderDishModel.setOrder(orderModel);
        orderDishModel.setDish(dishModel);
        orderDishModel.setAmount(amount);
        return orderDishModel;
    }
}
