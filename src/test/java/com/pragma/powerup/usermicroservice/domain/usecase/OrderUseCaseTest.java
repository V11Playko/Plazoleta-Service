package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserHaveOrderException;
import com.pragma.powerup.usermicroservice.configuration.Constants;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {
    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    IDishPersistencePort dishPersistencePort;

    @Mock
    IOrderPersistencePort orderPersistencePort;
    @Mock
    IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    private OrderUseCase orderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderUseCase = new OrderUseCase(restaurantPersistencePort, dishPersistencePort, orderPersistencePort, restaurantEmployeePersistencePort);
    }

    @Test
    void testNewOrder_UserHaveOrder_ThrowsUserHaveOrderException() {
        String idRestaurant = "1";
        String idClient = "1";

        when(orderPersistencePort.getNumberOfOrdersWithStateInPreparationPendingOrReady(Long.valueOf(idClient)))
                .thenReturn(1);

        assertThrows(UserHaveOrderException.class,
                () -> orderUseCase.newOrder(idRestaurant, idClient, Collections.emptyList()));

        verify(orderPersistencePort, never()).saveOrder(any(OrderModel.class), anyList());
    }

    @Test
    void listOrdersByState() {
        String orderState = "PENDING";
        int page = 0;
        int elementsPerPage = 10;
        String employeeEmail = "employee@example.com";

        RestaurantEmployeeModel employeeModel = new RestaurantEmployeeModel();
        employeeModel.setRestaurant(new RestaurantModel());

        when(restaurantEmployeePersistencePort.findByEmployeeEmail(employeeEmail))
                .thenReturn(Optional.of(employeeModel));

        List<OrderWithDishesModel> expectedOrders = Arrays.asList(new OrderWithDishesModel(), new OrderWithDishesModel());

        when(orderPersistencePort.getOrdersByRestaurantIdAndState(
                any(), anyInt(), anyInt(), anyString()))
                .thenReturn(expectedOrders);


        List<OrderWithDishesModel> orders = orderUseCase.listOrdersByState(orderState, page, elementsPerPage, employeeEmail);


        assertEquals(expectedOrders, orders);
        verify(restaurantEmployeePersistencePort, times(1)).findByEmployeeEmail(employeeEmail);
        verify(orderPersistencePort, times(1))
                .getOrdersByRestaurantIdAndState(employeeModel.getRestaurant().getId(), page, elementsPerPage, orderState);
    }


    @Test
    void assignOrder() {

        String employeeEmail = "employee@example.com";
        Long orderId = 1L;

        RestaurantModel restaurantModel = DomainData.obtainRestaurant();
        RestaurantEmployeeModel employeeModel = DomainData.getRestaurantEmployee(restaurantModel);
        OrderModel orderModel = DomainData.getOrderModel();
        orderModel.setId(orderId);
        orderModel.setState(Constants.ORDER_PENDING_STATE);
        CategoryDishModel categoryModel = DomainData.getCategoryModel();
        DishModel dishModel = DomainData.obtainDish(categoryModel, restaurantModel);
        OrdersDishesModel ordersDishesModel = DomainData.getOrderDishModel(dishModel, orderModel, 2);

        OrderWithDishesModel orderWithDishesModel = new OrderWithDishesModel();
        orderWithDishesModel.setOrderDishes(Collections.singletonList(ordersDishesModel));
        orderWithDishesModel.setId(orderModel.getId());
        orderWithDishesModel.setState(Constants.ORDER_PREPARATION_STATE);


        when(restaurantEmployeePersistencePort.findByEmployeeEmail(employeeEmail))
                .thenReturn(Optional.of(employeeModel));
        when(orderPersistencePort.getOrderByRestaurantIdAndOrderId(anyLong(), anyLong()))
                .thenReturn(Optional.of(orderModel));
        when(orderPersistencePort.saveOrderToOrderWithDishes(any(OrderModel.class)))
                .thenReturn(orderWithDishesModel);

        OrderWithDishesModel result = orderUseCase.assignOrder(employeeEmail, orderId);

        assertNotNull(result);
        assertEquals(Constants.ORDER_PREPARATION_STATE, orderModel.getState());
        assertEquals(employeeModel, orderModel.getEmailChef());
        verify(restaurantEmployeePersistencePort, times(1)).findByEmployeeEmail(employeeEmail);
        verify(orderPersistencePort, times(1)).getOrderByRestaurantIdAndOrderId(anyLong(), anyLong());
        verify(orderPersistencePort, times(1)).saveOrderToOrderWithDishes(any(OrderModel.class));
    }

}
