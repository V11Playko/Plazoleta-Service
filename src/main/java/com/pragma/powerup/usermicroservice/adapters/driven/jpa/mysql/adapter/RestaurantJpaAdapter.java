package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;


import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    @Override
    public RestaurantEntity saveRestaurant(RestaurantModel restaurantModel) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntityRestaurant(restaurantModel);
        return restaurantRepository.save(restaurantEntity);
    }

    @Override
    public RestaurantModel getRestaurant(Long id) {
        return restaurantEntityMapper.toRestaurantModel(restaurantRepository.findById(id).get());
    }

    @Override
    public List<RestaurantModel> listByPageAndElements(int page, int numbersOfElements) {
        PageRequest sortedByName = PageRequest.of(
                page, numbersOfElements,
                Sort.by("name").ascending()
        );
        Page<RestaurantEntity> restaurantEntities = restaurantRepository.findAll(sortedByName);

        return restaurantEntities.getContent().stream()
                .map(restaurantEntityMapper::toModelNoDishes)
                .collect(Collectors.toList());
    }
}
