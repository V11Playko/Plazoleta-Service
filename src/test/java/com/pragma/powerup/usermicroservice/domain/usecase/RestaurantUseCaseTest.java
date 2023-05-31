package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.feignModels.User;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private UserClient userClient;

    private AdminUseCase adminUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        adminUseCase = new AdminUseCase(restaurantPersistencePort, userClient);
    }

    @Test
    void testSaveRestaurant_ValidRole_SaveSuccessfully() {
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();
        restaurantModel.setIdOwner("1");

        User user = new User();
        user.setIdRole("2");

        when(userClient.getUser("1")).thenReturn(user);

        adminUseCase.saveRestaurant(restaurantModel);

        verify(restaurantPersistencePort, times(1)).saveRestaurant(restaurantModel);
    }
}