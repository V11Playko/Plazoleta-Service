package com.pragma.powerup.usermicroservice.domain.usecase;


import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.feignModels.User;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.ValidateRoleOwner;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;


public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final UserClient userClient;
    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, UserClient userClient) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClient = userClient;
    }
    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        //Traer usuario con el usecase
        try {
            User user = userClient.getOwner(restaurantModel.getIdOwner());
            if (user.getIdRole().equals("2")) {
                restaurantPersistencePort.saveRestaurant(restaurantModel);
            }
        } catch (RuntimeException e) {
            throw new ValidateRoleOwner();
        }

    }
}
