package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;


import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderStateType;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IOrderRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.pragma.powerup.usermicroservice.configuration.Constants;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotExist;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepository;
    private final IOrderRepository orderRepository;
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
                .map(restaurantEntityMapper::toRestaurantModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRestaurantById(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public Integer getNumberOfOrdersWithStateInPending(Long id) {
        return orderRepository.countPendingOrdersByRestaurantId(id);
    }

    @Override
    public void updateRestaurant(RestaurantModel restaurant) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurant.getId()).orElseThrow();
        restaurantEntity.setState(restaurant.getState());

        restaurantRepository.updateRestaurantState(restaurantEntity.getId(), Constants.PENDING_DELETE);
    }
}
