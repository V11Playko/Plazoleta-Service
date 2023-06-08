package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private UserClient userClient;
    @Mock
    IDishPersistencePort dishPersistencePort;

    private RestaurantUseCase restaurantUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, userClient);
    }

    @Test
    void testSaveRestaurant_ValidRole_SaveSuccessfully() {
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();
        restaurantModel.setIdOwner("1");

        User user = new User();
        user.setIdRole("2");

        when(userClient.getUser("1")).thenReturn(user);

        restaurantUseCase.saveRestaurant(restaurantModel);

        verify(restaurantPersistencePort, times(1)).saveRestaurant(restaurantModel);
    }

    @Test
    void listRestaurant() {
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();

        when(restaurantPersistencePort.listByPageAndElements(0, 2))
                .thenReturn(Collections.singletonList(restaurantModel));

        Assertions.assertInstanceOf(List.class, restaurantUseCase.listRestaurant(0, 2));
    }
}