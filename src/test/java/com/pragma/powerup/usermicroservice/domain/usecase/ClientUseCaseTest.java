package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientUseCaseTest {
    @InjectMocks
    ClientUseCase clientUseCase;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Test
    void listRestaurant() {
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();

        when(restaurantPersistencePort.listByPageAndElements(0, 2))
                .thenReturn(Collections.singletonList(restaurantModel));

        Assertions.assertInstanceOf(List.class, clientUseCase.listRestaurant(0, 2));
    }
}
