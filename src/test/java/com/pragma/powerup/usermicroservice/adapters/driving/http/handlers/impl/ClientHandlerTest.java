package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.ListRestaurantForClientResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListRestaurantClientMapper;
import com.pragma.powerup.usermicroservice.domain.api.IClientServicePort;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientHandlerTest {
    @InjectMocks
    ClientHandler clientHandler;
    @Mock
    IClientServicePort clientServicePort;
    @Mock
    IListRestaurantClientMapper listRestaurantClientMapper;

    @Test
    void listRestaurant() {
        // Datos de prueba
        int page = 0;
        int numberOfElements = 10;

        // Mock del resultado del IClientServicePort
        RestaurantModel restaurant = HttpData.obtainRestaurant();

        List<RestaurantModel> mockResult = Arrays.asList(restaurant);
        when(clientServicePort.listRestaurant(page, numberOfElements)).thenReturn(mockResult);

        // Mock del resultado del IListRestaurantClientMapper
        ListRestaurantForClientResponseDto dto = new ListRestaurantForClientResponseDto("Restaurant 1", "http//aja.com");
        when(listRestaurantClientMapper.toDto(restaurant)).thenReturn(dto);

        // Llamada al método bajo prueba
        List<ListRestaurantForClientResponseDto> result = clientHandler.listRestaurant(page, numberOfElements);

        // Verificaciones
        verify(clientServicePort).listRestaurant(page, numberOfElements);
        verify(listRestaurantClientMapper).toDto(restaurant);

        // Verificación del resultado
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }
}