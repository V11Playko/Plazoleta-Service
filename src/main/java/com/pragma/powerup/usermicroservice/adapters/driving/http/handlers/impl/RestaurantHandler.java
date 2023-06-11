package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.ListRestaurantForClientResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.RestaurantResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IRestaurantHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListRestaurantClientMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IRestaurantResponseMapper;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.ValidateRoleOwner;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {
    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;
    private final IListRestaurantClientMapper listRestaurantClientMapper;


    /**
     * Created a restaurant
     *
     * @param restaurantRequestDto - restaurant information
     * */
    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        RestaurantModel restaurantModel = restaurantRequestMapper.toRestaurantRequest(restaurantRequestDto);
        restaurantServicePort.saveRestaurant(restaurantModel);
    }

    @Override
    public RestaurantResponseDto getRestaurant(Long id) {
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(id);
        return restaurantResponseMapper.toResponseRestaurant(restaurantModel);
    }

    /**
     * Paginated list of restaurants
     *
     * @param page - page number to list
     * @param numberOfElements - elements to show per page
     * @return list paginated and alphabetically sorted
     * */
    @Override
    public List<ListRestaurantForClientResponseDto> listRestaurant(int page, int numberOfElements) {
        return restaurantServicePort.listRestaurant(page, numberOfElements).stream()
                .map(listRestaurantClientMapper::toDto)
                .collect(Collectors.toList());
    }
}
