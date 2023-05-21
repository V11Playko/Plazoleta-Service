package com.pragma.powerup.usermicroservice.domain.usecase;


import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.feignModels.User;
import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.DomainException;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;


public class DishUseCase implements IDishServicePort {
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantServicePort restaurantServicePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantServicePort restaurantServicePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantServicePort = restaurantServicePort;
    }

    @Override
    public void saveDish(DishModel dishModel, String id_owner) {

        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(dishModel.getRestaurant().getId());

            if (!restaurantModel.getIdOwner().equals(id_owner)) {
                throw new UserNotIsOwner();
            }
            dishPersistencePort.saveDish(dishModel);
    }

    @Override
    public DishModel getDish(Long id) {
        return dishPersistencePort.getDish(id);
    }

    @Override
    public void updateDish(DishModel dishModel,String id_owner) {
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(dishModel.getRestaurant().getId());

        if (!restaurantModel.getIdOwner().equals(id_owner)) {
            throw new UserNotIsOwner();
        }
        dishPersistencePort.updateDish(dishModel);
    }
}
