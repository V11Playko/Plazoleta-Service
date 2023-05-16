package com.pragma.powerup.usermicroservice.domain.ports;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;

public interface IDishPersistencePort {
    DishEntity saveDish(DishModel dishModel);
    DishModel getDish(Long id);
    void updateDish(DishModel dishModel);
}
