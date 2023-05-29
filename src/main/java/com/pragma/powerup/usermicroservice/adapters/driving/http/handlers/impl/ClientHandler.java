package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.ListRestaurantForClientResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IClientHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListRestaurantClientMapper;
import com.pragma.powerup.usermicroservice.domain.api.IClientServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientHandler implements IClientHandler {
    private final IClientServicePort clientServicePort;
    private final IListRestaurantClientMapper listRestaurantClientMapper;

    @Override
    public List<ListRestaurantForClientResponseDto> listRestaurant(int page, int numberOfElements) {
        return clientServicePort.listRestaurant(page, numberOfElements).stream()
                .map(listRestaurantClientMapper::toDto)
                .collect(Collectors.toList());
    }
}
