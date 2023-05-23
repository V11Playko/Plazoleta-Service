package com.pragma.powerup.usermicroservice.domain.api;


import com.pragma.powerup.usermicroservice.domain.model.DishModel;

public interface IOwnerServicePort {
    void saveDish(DishModel dishModel, String idOwner);
    DishModel getDish(Long id);
    void updateDish(DishModel dishModel,String idOwner);
    void updateDishState(DishModel dishModel, String idOwner);
}
