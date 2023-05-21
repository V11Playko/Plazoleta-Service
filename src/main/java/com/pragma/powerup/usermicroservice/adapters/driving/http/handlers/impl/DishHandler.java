package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IDishHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishRequestMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishResponseMapper;
import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {
    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;
    @Override
    public void saveDish(DishRequestDto dishRequestDto) {
        DishModel dishModel = dishRequestMapper.toDishRequest(dishRequestDto);
        dishModel.setState(true);
        dishServicePort.saveDish(dishModel);
    }

    @Override
    public DishResponseDto getDish(Long id) {
        DishModel dish = dishServicePort.getDish(id);
        return dishResponseMapper.toResponseDish(dish);
    }

    @Override
    public void updateDish(DishUpdateRequest dishUpdateRequest) {
        DishModel dish = dishServicePort.getDish(dishUpdateRequest.getId());

        if(Strings.isNotBlank(dishUpdateRequest.getDescription()) || Strings.isNotEmpty(dishUpdateRequest.getDescription())) dish.setDescription(dishUpdateRequest.getDescription());
        if(dishUpdateRequest.getPrice() > 0) dish.setPrice(dishUpdateRequest.getPrice());

        dishServicePort.updateDish(dish);
    }
}
