package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserHaveOrderException;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientUseCaseTest {
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    private ClientUseCase clientUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientUseCase = new ClientUseCase(restaurantPersistencePort, dishPersistencePort, orderPersistencePort);
    }

    @Test
    void listRestaurant() {
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();

        when(restaurantPersistencePort.listByPageAndElements(0, 2))
                .thenReturn(Collections.singletonList(restaurantModel));

        Assertions.assertInstanceOf(List.class, clientUseCase.listRestaurant(0, 2));
    }

    @Test
    void getDishesCategorizedByRestaurant() {
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();
        CategoryDishModel categoryModel = DomainData.getCategoryModel();
        DishModel dishModel = DomainData.obtainDish(categoryModel, restaurantModel);

        when(restaurantPersistencePort.getRestaurant(restaurantModel.getId())).thenReturn(restaurantModel);
        when(dishPersistencePort.listDishesByRestaurant(String.valueOf(restaurantModel.getId()), 0, 2))
                .thenReturn(Collections.singletonList(dishModel));

        Assertions.assertInstanceOf(List.class, clientUseCase.getDishesCategorizedByRestaurant
                (String.valueOf(restaurantModel.getId()), 0, 2));
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
                () -> clientUseCase.newOrder(idRestaurant, idClient, Collections.emptyList()));

        verify(orderPersistencePort, never()).saveOrder(any(OrderModel.class), anyList());
    }
}
