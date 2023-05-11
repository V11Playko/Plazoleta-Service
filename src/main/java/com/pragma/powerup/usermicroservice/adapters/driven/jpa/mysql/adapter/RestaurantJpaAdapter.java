package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;


import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    @Override
    public RestaurantEntity saveRestaurant(RestaurantModel restaurantModel) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntityRestaurant(restaurantModel);
        return restaurantRepository.save(restaurantEntity);
    }
}
