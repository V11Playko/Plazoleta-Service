package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.UpdateDishStateRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryDishesResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IDishHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishRequestMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishResponseMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListDishesCategoryByRestaurantMapper;
import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotExist;
import com.pragma.powerup.usermicroservice.domain.exceptions.SameStateException;
import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {
    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;
    private final IListDishesCategoryByRestaurantMapper listDishesCategoryByRestaurantMapper;
    @Override
    public void saveDish(DishRequestDto dishRequestDto, String idOwner) {
        DishModel dishModel = dishRequestMapper.toDishRequest(dishRequestDto);
        dishModel.setState(true);
        dishServicePort.saveDish(dishModel, idOwner);
    }

    @Override
    public DishResponseDto getDish(Long id) {
        DishModel dish = dishServicePort.getDish(id);
        return dishResponseMapper.toResponseDish(dish);
    }

    @Override
    public void updateDish(DishUpdateRequest dishUpdateRequest,String idOwner) {
        DishModel dish = dishServicePort.getDish(dishUpdateRequest.getId());

        if(Strings.isNotBlank(dishUpdateRequest.getDescription()) || Strings.isNotEmpty(dishUpdateRequest.getDescription())) dish.setDescription(dishUpdateRequest.getDescription());
        if(dishUpdateRequest.getPrice() > 0) dish.setPrice(dishUpdateRequest.getPrice());

        dishServicePort.updateDish(dish, idOwner);
    }

    @Override
    public void updateState(UpdateDishStateRequestDto dishUpdateStateRequestDto, String idOwner) {
        DishModel dish = dishServicePort.getDish(dishUpdateStateRequestDto.getDishId());

        if (dishUpdateStateRequestDto.isState() && dish.getState()) {
            throw new SameStateException();
        }
        dish.setState(dishUpdateStateRequestDto.isState());

        dishServicePort.updateDishState(dish, idOwner);
    }

    @Override
    public List<CategoryDishesResponseDto> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage) {
        List<CategoryWithDishesModel> categoryWithDishes;

        try {
            categoryWithDishes = dishServicePort.getDishesCategorizedByRestaurant(idRestaurant, page, elementsXpage);
        } catch (RestaurantNotExist e) {
            throw new RestaurantNotExist();
        }
        return categoryWithDishes.stream()
                .map(listDishesCategoryByRestaurantMapper::toDto)
                .collect(Collectors.toList());
    }
}
