package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantHaveOrdersPending;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantPendingDeleteException;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private UserClient userClient;
    @Mock
    IDishPersistencePort dishPersistencePort;
    @Mock
    IOrderPersistencePort orderPersistencePort;
    @Mock
    IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    private RestaurantUseCase restaurantUseCase;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, dishPersistencePort, orderPersistencePort, restaurantEmployeePersistencePort, userClient);
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

    @Test
    void testDeleteRestaurant_NoOrdersPending_Success() {
        Long restaurantId = 1L;

        // Mockear el restaurante sin órdenes pendientes
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);
        restaurant.setState("ACTIVE");

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(restaurant);
        when(restaurantPersistencePort.getNumberOfOrdersWithStateInPending(restaurantId)).thenReturn(0);

        // Mockear las llamadas a los métodos relacionados
        List<DishModel> emptyDishes = Collections.emptyList();
        when(dishPersistencePort.getDishesByRestaurantId(restaurantId)).thenReturn(emptyDishes);
        when(orderPersistencePort.getOrdersByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());
        when(restaurantEmployeePersistencePort.getEmployeesByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());

        // Ejecutar el método a probar
        assertDoesNotThrow(() -> restaurantUseCase.deleteRestaurant(restaurantId));

        // Verificar que se llamaron a los métodos relacionados
        verify(restaurantPersistencePort).deleteRestaurantById(restaurantId);
        for (DishModel dish : emptyDishes) {
            verify(dishPersistencePort).deleteDishById(dish.getId());
        }
        verify(orderPersistencePort, times(0)).deleteOrderById(anyLong());
        verify(restaurantEmployeePersistencePort, times(0)).deleteEmployeeByEmailAndRestaurantId(anyString(), anyLong());
    }

    @Test
    void testDeleteRestaurant_OrdersPending_ExceptionThrown() {
        Long restaurantId = 1L;

        // Mockear el restaurante con órdenes pendientes
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);
        restaurant.setState("ACTIVE");

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(restaurant);
        when(restaurantPersistencePort.getNumberOfOrdersWithStateInPending(restaurantId)).thenReturn(1);

        // Ejecutar el método a probar y verificar que se lance la excepción esperada
        assertThrows(RestaurantHaveOrdersPending.class, () -> restaurantUseCase.deleteRestaurant(restaurantId));
    }

    @Test
    void testDeleteRestaurant_PendingDeleteState_ExceptionThrown() {
        Long restaurantId = 1L;

        // Mockear el restaurante en estado PENDING_DELETE
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);
        restaurant.setState("PENDING_DELETE");

        when(restaurantPersistencePort.getRestaurant(restaurantId)).thenReturn(restaurant);

        // Ejecutar el método a probar y verificar que se lance la excepción esperada
        assertThrows(RestaurantPendingDeleteException.class, () -> restaurantUseCase.deleteRestaurant(restaurantId));
    }
}