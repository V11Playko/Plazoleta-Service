package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserHaveOrderException;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    private OrderUseCase orderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderUseCase = new OrderUseCase(restaurantPersistencePort, dishPersistencePort, orderPersistencePort);
    }

    @Test
    void testNewOrder_UserHaveOrder_ThrowsUserHaveOrderException() {
        // Arrange
        String idRestaurant = "1";
        String idClient = "1";

        when(orderPersistencePort.getNumberOfOrdersWithStateInPreparationPendingOrReady(Long.valueOf(idClient)))
                .thenReturn(1);

        // Act & Assert
        assertThrows(UserHaveOrderException.class,
                () -> orderUseCase.newOrder(idRestaurant, idClient, Collections.emptyList()));

        verify(orderPersistencePort, never()).saveOrder(any(OrderModel.class), anyList());
    }
}
