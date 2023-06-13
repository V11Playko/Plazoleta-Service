package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.NoDataFoundException;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.OrderDishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.AssignOrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IAssignOrderResponseMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListOrdersResponseMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderWithDishesResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IOrderDishRequestMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IOrderResponseMapper;
import com.pragma.powerup.usermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.EmployeeNotBelongAnyRestaurant;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    private final IOrderServicePort orderServicePort;
    private final IOrderDishRequestMapper orderDishRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;
    private final IListOrdersResponseMapper listOrdersResponseMapper;
    private final IAssignOrderResponseMapper assignOrderResponseMapper;
    private final UserClient userClient;

    @Override
    public OrderResponseDto getOrder(Long id) {
        OrderModel orderModel = orderServicePort.getOrder(id);
        return orderResponseMapper.toResponse(orderModel);
    }

    /**
     * Creates a new order for a client
     *
     * @param ordersDishesModels - dishes the client wants
     * @param idRestaurant - the restaurant the dishes belongs to
     * @param idClient - client id
     * @return list of dishes in the order and the amount of each one
     * */
    @Override
    public void newOrder(String idRestaurant, String idClient, List<OrderDishRequestDto> ordersDishesModels) {
        List<OrdersDishesModel> orderDishes = new ArrayList<>();
        ordersDishesModels.forEach(dish -> orderDishes.add(orderDishRequestMapper.toModel(dish)));

        orderServicePort.newOrder(idRestaurant, idClient, orderDishes);
    }

    /**
     * Lists orders by state
     *
     * @param orderState - order state to filter
     * @param page - page number to show. For pagination
     * @param elementsPerPage - elements to show per page. For pagination
     * @throws EmployeeNotBelongAnyRestaurant - Employee does not belong to any restaurant
     * @return list of orders with its dishes
     */
    @Override
    public List<OrderWithDishesResponseDto> listOrdersByState(String orderState, int page, int elementsPerPage, String employeeEmail) {
        String email = String.valueOf(userClient.getUserByEmail(employeeEmail).getEmail());
        List<OrderWithDishesModel> orders;

        try{
            orders = orderServicePort.listOrdersByState(orderState, page, elementsPerPage, email);
        } catch (EmployeeNotBelongAnyRestaurant e) {
            throw new EmployeeNotBelongAnyRestaurant();
        }

        return orders.stream()
                .map(listOrdersResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Assign multiple orders to an employee
     *
     * @param ordersId - list of ids corresponding to orders
     * @param employeeEmail - employee email
     * @throws NoDataFoundException - some order doesn't exist
     * @return list of assigned orders
     */
    @Override
    public List<AssignOrderResponseDto> assignOrdersToEmployee(List<Long> ordersId, String employeeEmail) {
        String emailEmployee = userClient.getUserByEmail(employeeEmail).getEmail();

        List<OrderWithDishesModel> ordersChanged = new ArrayList<>();

        for (Long orderId : ordersId) {
            try {
                OrderWithDishesModel order = orderServicePort.assignOrder(emailEmployee, orderId);
                ordersChanged.add(order);

            } catch (NoDataFoundException e) {
                throw new NoDataFoundException();
            }
        }
        return ordersChanged.stream()
                .map(assignOrderResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Changes the order to ready
     *
     * @param orderId - order id
     * @param employeeEmail - employee email
     * */
    @Override
    public void changeOrderToReady(Long orderId, String employeeEmail) {
        String emailEmployee = userClient.getUserByEmail(employeeEmail).getEmail();

        orderServicePort.changeOrderToReady(emailEmployee, orderId);
    }

    /**
     * Changes the order to 'delivered'
     *
     * @param orderId order id
     * @param employeeEmail - employee email
     * @param securityCode - order security code
     * */
    @Override
    public void changeOrderToDelivered(Long orderId, String employeeEmail, String securityCode) {
        String emailEmployee = userClient.getUserByEmail(employeeEmail).getEmail();

        orderServicePort.changeOrderToDelivered(emailEmployee, orderId, securityCode);
    }

    /**
     * Cancel an order from the client
     *
     * @param orderId order id to cancel
     * @param clientEmail - client email
     * */
    @Override
    public void cancelOrder(Long orderId, String clientEmail) {
        String emailClient = userClient.getUserByEmail(clientEmail).getEmail();

        orderServicePort.cancelOrder(emailClient, orderId);
    }
}
