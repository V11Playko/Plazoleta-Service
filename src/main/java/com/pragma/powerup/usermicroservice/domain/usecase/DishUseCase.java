package com.pragma.powerup.usermicroservice.domain.usecase;


import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.feignModels.User;
import com.pragma.powerup.usermicroservice.domain.api.IOwnerServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.DomainException;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;

import java.util.Optional;

public class OwnerUseCase implements IOwnerServicePort {
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final UserClient userClient;

    public OwnerUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, UserClient userClient) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClient = userClient;
    }

    @Override
    public void saveDish(DishModel dishModel) {
        try {
            User user = userClient.getOwner(dishModel.getRestaurant().getIdOwner());
            // Se valida que sea un usuario owner
            if (user.getIdRole().equals("2")) {
                // Obtener el ID del propietario del restaurante
                String ownerId = dishModel.getRestaurant().getIdOwner();
                //Obtener el ID del usuario
                String userId = String.valueOf(user.getId());

                // Verificar si el usuario es el propietario del restaurante
                if (userId.equals(ownerId)) {
                    dishPersistencePort.saveDish(dishModel);
                } else throw new UserNotIsOwner();
            }
        } catch (RuntimeException e) {
            throw new DomainException("Prueba");
        }
    }

    @Override
    public DishModel getDish(Long id) {
        return dishPersistencePort.getDish(id);
    }

    @Override
    public void updateDish(DishModel dishModel) {
        dishPersistencePort.updateDish(dishModel);
    }

    @Override
    public Optional<RestaurantModel> getRestaurantByOwnerId(Long id) {
        Optional<RestaurantModel> restaurantModel = this.restaurantPersistencePort.getRestaurantById(id);
        if (restaurantModel == null) {
            throw new DomainException("No restaurant found for the owner id specified");
        }
        return restaurantModel;
    }
}
