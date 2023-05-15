package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantJpaAdapterTest {
    @InjectMocks
    RestaurantJpaAdapter restaurantJpaAdapter;
    @Mock
    IRestaurantRepository restaurantRepository;
    @Mock
    IRestaurantEntityMapper restaurantMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRestaurant() {
        RestaurantModel restaurant = MySqlData.obtainRestaurant();
        RestaurantEntity restaurantEntity = MySqlData.obtainRestaurantEntity();

        when(restaurantMapper.toEntityRestaurant(restaurant)).thenReturn(restaurantEntity);
        restaurantJpaAdapter.saveRestaurant(restaurant);

        verify(restaurantRepository).save(restaurantEntity);

    }
}