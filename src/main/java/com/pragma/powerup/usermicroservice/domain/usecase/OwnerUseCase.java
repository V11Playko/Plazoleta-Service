package com.pragma.powerup.usermicroservice.domain.usecase;


import com.pragma.powerup.usermicroservice.domain.api.IOwnerServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IAdminServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;


public class OwnerUseCase implements IOwnerServicePort {
    private final IDishPersistencePort dishPersistencePort;
    private final IAdminServicePort restaurantServicePort;

    public OwnerUseCase(IDishPersistencePort dishPersistencePort, IAdminServicePort restaurantServicePort) {
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
