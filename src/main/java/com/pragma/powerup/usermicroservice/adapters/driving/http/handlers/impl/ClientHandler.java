package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.OrderDishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryDishesResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.ListRestaurantForClientResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IClientHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListDishesCategoryByRestaurantMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListRestaurantClientMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IOrderDishRequestMapper;
import com.pragma.powerup.usermicroservice.domain.api.IClientServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotExist;
import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
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
public class ClientHandler implements IClientHandler {
    private final IClientServicePort clientServicePort;
    private final IListRestaurantClientMapper listRestaurantClientMapper;
    private final IListDishesCategoryByRestaurantMapper listDishesCategoryByRestaurantMapper;
    private final IOrderDishRequestMapper orderDishRequestMapper;

    @Override
    public List<ListRestaurantForClientResponseDto> listRestaurant(int page, int numberOfElements) {
        return clientServicePort.listRestaurant(page, numberOfElements).stream()
                .map(listRestaurantClientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDishesResponseDto> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage) {
        List<CategoryWithDishesModel> categoryWithDishes;

        try {
            categoryWithDishes = clientServicePort.getDishesCategorizedByRestaurant(idRestaurant, page, elementsXpage);
        } catch (RestaurantNotExist e) {
            throw new RestaurantNotExist();
        }
        return categoryWithDishes.stream()
                .map(listDishesCategoryByRestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void newOrder(String idRestaurant, String idClient, List<OrderDishRequestDto> ordersDishesModels) {
        List<OrdersDishesModel> orderDishes = new ArrayList<>();
        ordersDishesModels.forEach(dish -> orderDishes.add(orderDishRequestMapper.toModel(dish)));

        clientServicePort.newOrder(idRestaurant, idClient, orderDishes);
    }
}
