package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishRequestMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishResponseMapper;
import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
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
        DishModel dishModel = HttpData.obtainDish();
        DishRequestDto dishRequestDto = HttpData.obtainDishRequest();

        when(dishRequestMapper.toDishRequest(dishRequestDto)).thenReturn(dishModel);

        dishHandler.saveDish(dishRequestDto);

        verify(dishServicePort).saveDish(dishModel);
    }

    @Test
    void getDish() {
        DishModel dish = HttpData.obtainDish();

        when(dishServicePort.getDish(anyLong())).thenReturn(dish);
        dishHandler.getDish(anyLong());

        verify(dishResponseMapper).toResponseDish(dish);
    }

    @Test
    void updateDish() {
        DishModel dish = HttpData.obtainDish();
        DishUpdateRequest dishUpdateRequest = HttpData.obtainDishUpdate();

        when(dishServicePort.getDish(anyLong())).thenReturn(dish);
        dishHandler.updateDish(dishUpdateRequest);

        verify(dishServicePort).updateDish(dish);
    }
}