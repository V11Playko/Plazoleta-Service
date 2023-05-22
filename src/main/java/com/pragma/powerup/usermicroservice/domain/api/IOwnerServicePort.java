package com.pragma.powerup.usermicroservice.domain.api;


import com.pragma.powerup.usermicroservice.domain.model.DishModel;

public interface IOwnerServicePort {
    void saveDish(DishModel dishModel, String id_owner);
    DishModel getDish(Long id);
    void updateDish(DishModel dishModel,String id_owner);
}
