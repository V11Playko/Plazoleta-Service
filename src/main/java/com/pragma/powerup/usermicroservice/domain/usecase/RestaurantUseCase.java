package com.pragma.powerup.usermicroservice.domain.usecase;


import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.ValidateRoleOwner;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;

import java.util.List;


public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final UserClient userClient;
    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, UserClient userClient) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClient = userClient;
    }

    /**
     * Creates a restaurant, validating first if the user id correspond to a
     * restaurant owner in the user service
     *
     * @param restaurantModel - restaurant information
     * @throws ValidateRoleOwner - Role is not owned by an owner
     *
     * */
    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        //Traer usuario con el usecase
        try {
            User user = userClient.getUser(restaurantModel.getIdOwner());
            if (user.getIdRole().equals("2")) {
                restaurantPersistencePort.saveRestaurant(restaurantModel);
            }
        } catch (RuntimeException e) {
            throw new ValidateRoleOwner();
        }

    }

    @Override
    public RestaurantModel getRestaurant(Long id) {
        return restaurantPersistencePort.getRestaurant(id);
    }

    /**
     * List restaurants
     *
     * @param page - number of the page of the restaurants
     * @param numberOfElements - max number of restaurants to be returned in the list
     * @return list of the restaurants sorted alphabetically
     * */
    @Override
    public List<RestaurantModel> listRestaurant(int page, int numberOfElements) {
        return restaurantPersistencePort.listByPageAndElements(page, numberOfElements);
    }
}
