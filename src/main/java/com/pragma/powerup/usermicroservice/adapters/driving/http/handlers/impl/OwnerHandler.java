package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.UpdateDishStateRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IOwnerHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishRequestMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishResponseMapper;
import com.pragma.powerup.usermicroservice.domain.api.IOwnerServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IAdminServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.SameStateException;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class OwnerHandler implements IOwnerHandler {
    private final IOwnerServicePort ownerServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;
    private final IAdminServicePort restaurantServicePort;
    private final UserClient userClient;
    @Override
    public void saveDish(DishRequestDto dishRequestDto, String idOwner) {
        DishModel dishModel = dishRequestMapper.toDishRequest(dishRequestDto);
        dishModel.setState(true);
        ownerServicePort.saveDish(dishModel, idOwner);
    }

    @Override
    public DishResponseDto getDish(Long id) {
        DishModel dish = ownerServicePort.getDish(id);
        return dishResponseMapper.toResponseDish(dish);
    }

    @Override
    public void updateDish(DishUpdateRequest dishUpdateRequest,String idOwner) {
        DishModel dish = ownerServicePort.getDish(dishUpdateRequest.getId());

        if(Strings.isNotBlank(dishUpdateRequest.getDescription()) || Strings.isNotEmpty(dishUpdateRequest.getDescription())) dish.setDescription(dishUpdateRequest.getDescription());
        if(dishUpdateRequest.getPrice() > 0) dish.setPrice(dishUpdateRequest.getPrice());

        ownerServicePort.updateDish(dish, idOwner);
    }

    @Override
    public void updateState(UpdateDishStateRequestDto dishUpdateStateRequestDto, String idOwner) {
        DishModel dish = ownerServicePort.getDish(dishUpdateStateRequestDto.getDishId());

        if (dishUpdateStateRequestDto.isState() && dish.getState()) {
            throw new SameStateException();
        }
        dish.setState(dishUpdateStateRequestDto.isState());

        ownerServicePort.updateDishState(dish, idOwner);
    }


}
