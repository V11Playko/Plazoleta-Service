package com.pragma.powerup.usermicroservice.domain.api;


import com.pragma.powerup.usermicroservice.domain.model.DishModel;

public interface IDishServicePort {
    void saveDish(DishModel dishModel);
    DishModel getDish(Long id);
    void updateDish(DishModel dishModel);
}
