package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.OrderDishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IOrderDishRequestMapper;
import com.pragma.powerup.usermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    private final IOrderServicePort clientServicePort;
    private final IOrderDishRequestMapper orderDishRequestMapper;

    @Override
    public void newOrder(String idRestaurant, String idClient, List<OrderDishRequestDto> ordersDishesModels) {
        List<OrdersDishesModel> orderDishes = new ArrayList<>();
        ordersDishesModels.forEach(dish -> orderDishes.add(orderDishRequestMapper.toModel(dish)));

        clientServicePort.newOrder(idRestaurant, idClient, orderDishes);
    }
}
