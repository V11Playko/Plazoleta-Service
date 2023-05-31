package com.pragma.powerup.usermicroservice.domain.api;


import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;

import java.util.List;

public interface IDishServicePort {
    void saveDish(DishModel dishModel, String idOwner);
    DishModel getDish(Long id);
    void updateDish(DishModel dishModel,String idOwner);
    void updateDishState(DishModel dishModel, String idOwner);
    List<CategoryWithDishesModel> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage);

}
