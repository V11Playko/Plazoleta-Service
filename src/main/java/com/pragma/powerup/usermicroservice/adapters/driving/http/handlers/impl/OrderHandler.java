package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.NoDataFoundException;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.OrderDishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.AssignOrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IAssignOrderResponseMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListOrdersResponseMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IOrderDishRequestMapper;
import com.pragma.powerup.usermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.EmployeeNotBelongAnyRestaurant;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    private final IOrderServicePort orderServicePort;
    private final IOrderDishRequestMapper orderDishRequestMapper;
    private final IListOrdersResponseMapper listOrdersResponseMapper;
    private final IAssignOrderResponseMapper assignOrderResponseMapper;
    private final UserClient userClient;

    @Override
    public void newOrder(String idRestaurant, String idClient, List<OrderDishRequestDto> ordersDishesModels) {
        List<OrdersDishesModel> orderDishes = new ArrayList<>();
        ordersDishesModels.forEach(dish -> orderDishes.add(orderDishRequestMapper.toModel(dish)));

        orderServicePort.newOrder(idRestaurant, idClient, orderDishes);
    }

    @Override
    public List<OrderResponseDto> listOrdersByState(String orderState, int page, int elementsPerPage, String employeeEmail) {
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

    @Override
    public void changeOrderToReady(Long orderId, String employeeEmail) {
        String emailEmployee = userClient.getUserByEmail(employeeEmail).getEmail();

        orderServicePort.changeOrderToReady(emailEmployee, orderId);
    }

    @Override
    public void changeOrderToDelivered(Long orderId, String employeeEmail, String securityCode) {
        String emailEmployee = userClient.getUserByEmail(employeeEmail).getEmail();

        orderServicePort.changeOrderToDelivered(emailEmployee, orderId, securityCode);
    }

    @Override
    public void cancelOrder(Long orderId, String clientEmail) {
        String emailClient = userClient.getUserByEmail(clientEmail).getEmail();

        orderServicePort.cancelOrder(emailClient, orderId);
    }
}
