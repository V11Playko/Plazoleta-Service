package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.RestaurantRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.ListRestaurantForClientResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListRestaurantClientMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantHandlerTest {

    @InjectMocks
    private RestaurantHandler restaurantHandler;
    @Mock
    IRestaurantServicePort restaurantServicePort;
    @Mock
    IRestaurantRequestMapper restaurantRequestMapper;
    @Mock
    IListRestaurantClientMapper listRestaurantClientMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRestaurant() {
        RestaurantModel restaurantModel = HttpData.obtainRestaurant();
        RestaurantRequestDto restaurantRequest = HttpData.obtainRestaurantRequest();

        when(restaurantRequestMapper.toRestaurantRequest(restaurantRequest)).thenReturn(restaurantModel);
        restaurantHandler.saveRestaurant(restaurantRequest);

        verify(restaurantServicePort).saveRestaurant(restaurantModel);
    }

    @Test
    void listRestaurant() {
        // Datos de prueba
        int page = 0;
        int numberOfElements = 10;

        // Mock del resultado del IOrderServicePort
        RestaurantModel restaurant = HttpData.obtainRestaurant();

        List<RestaurantModel> mockResult = Arrays.asList(restaurant);
        when(restaurantServicePort.listRestaurant(page, numberOfElements)).thenReturn(mockResult);

        // Mock del resultado del IListRestaurantClientMapper
        ListRestaurantForClientResponseDto dto = new ListRestaurantForClientResponseDto("Restaurant 1", "http//aja.com");
        when(listRestaurantClientMapper.toDto(restaurant)).thenReturn(dto);

        // Llamada al método bajo prueba
        List<ListRestaurantForClientResponseDto> result = restaurantHandler.listRestaurant(page, numberOfElements);

        // Verificaciones
        verify(restaurantServicePort).listRestaurant(page, numberOfElements);
        verify(listRestaurantClientMapper).toDto(restaurant);

        // Verificación del resultado
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }
}