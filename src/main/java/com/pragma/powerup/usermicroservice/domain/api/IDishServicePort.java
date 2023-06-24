package com.pragma.powerup.usermicroservice.domain.api;


import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.domain.model.CategoryAveragePriceModel;
import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;

import java.util.List;
import java.util.Map;

public interface IDishServicePort {
    void saveDish(DishModel dishModel, String idOwner);
    DishModel getDish(Long id);
    void updateDish(DishModel dishModel,String idOwner);
    void updateDishState(DishModel dishModel, String idOwner);
    RestaurantEmployeeModel createEmployee(User user, Long idRestaurant, String emailEmployee);
    List<CategoryWithDishesModel> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage);
    List<CategoryAveragePriceModel> calculateAverageByCategory(String idOwner);
}
