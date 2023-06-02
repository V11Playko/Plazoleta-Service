package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.UpdateDishStateRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishRequestMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishResponseMapper;
import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishHandlerTest {
    @InjectMocks
    DishHandler dishHandler;
    @Mock
    IDishServicePort dishServicePort;
    @Mock
    IDishRequestMapper dishRequestMapper;
    @Mock
    IDishResponseMapper dishResponseMapper;

    @Test
    void saveDish() {
        RestaurantModel restaurant = HttpData.obtainRestaurant();
        CategoryDishModel categoryModel = HttpData.getCategoryModel();
        DishModel dishModel = HttpData.obtainDish(categoryModel, restaurant);
        DishRequestDto dishRequestDto = HttpData.obtainDishRequest();
        String idOwner = "2";

        when(dishRequestMapper.toDishRequest(dishRequestDto)).thenReturn(dishModel);

        dishHandler.saveDish(dishRequestDto, idOwner);

        verify(dishServicePort).saveDish(dishModel, idOwner);
    }

    @Test
    void getDish() {
        RestaurantModel restaurant = HttpData.obtainRestaurant();
        CategoryDishModel categoryModel = HttpData.getCategoryModel();
        DishModel dishModel = HttpData.obtainDish(categoryModel, restaurant);

        when(dishServicePort.getDish(anyLong())).thenReturn(dishModel);
        dishHandler.getDish(anyLong());

        verify(dishResponseMapper).toResponseDish(dishModel);
    }

    @Test
    void updateDish() {
        RestaurantModel restaurant = HttpData.obtainRestaurant();
        CategoryDishModel categoryModel = HttpData.getCategoryModel();
        DishModel dishModel = HttpData.obtainDish(categoryModel, restaurant);
        DishUpdateRequest dishUpdateRequest = HttpData.obtainDishUpdate();
        String idOwner = "2";

        when(dishServicePort.getDish(anyLong())).thenReturn(dishModel);
        dishHandler.updateDish(dishUpdateRequest, idOwner);

        verify(dishServicePort).updateDish(dishModel, idOwner);
    }

    @Test
    void updateDishState() {
        RestaurantModel restaurant = HttpData.obtainRestaurant();
        CategoryDishModel categoryModel = HttpData.getCategoryModel();
        DishModel dishModel = HttpData.obtainDish(categoryModel, restaurant);
        UpdateDishStateRequestDto dishRequest = HttpData.obtainDishState();
        String idOwner = "2";

        when(dishServicePort.getDish(anyLong())).thenReturn(dishModel);
        dishHandler.updateState(dishRequest, idOwner);

        verify(dishServicePort).updateDishState(dishModel, idOwner);
    }
}