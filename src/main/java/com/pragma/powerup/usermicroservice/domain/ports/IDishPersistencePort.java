package com.pragma.powerup.usermicroservice.domain.ports;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;

import java.util.List;

public interface IDishPersistencePort {
    DishEntity saveDish(DishModel dishModel);
    DishModel getDish(Long id);
    void updateDish(DishModel dishModel);
    void updateSate(DishModel dishModel);
    List<DishModel> listDishesByRestaurant(String idRestaurant, int page, int elementsXpage);
}
