package com.pragma.powerup.usermicroservice.domain.ports;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;

import java.util.List;

public interface IRestaurantPersistencePort {
    RestaurantEntity saveRestaurant(RestaurantModel restaurantModel);
    RestaurantModel getRestaurant(Long id);
    List<RestaurantModel> listByPageAndElements(int page, int numbersOfElements);
    void deleteRestaurantById(Long id);
    Integer getNumberOfOrdersWithStateInPending(Long id);
    void updateRestaurant(RestaurantModel restaurant);
}
