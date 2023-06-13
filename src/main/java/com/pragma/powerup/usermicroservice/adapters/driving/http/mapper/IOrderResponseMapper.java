package com.pragma.powerup.usermicroservice.adapters.driving.http.mapper;


import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderWithDishesResponseDto;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {
    @Mapping(target = "restaurant", source = "restaurant.id")
    OrderWithDishesResponseDto toResponseOrder(OrderModel orderModel);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "idClient", source = "idClient")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "securityPin", source = "securityPin")
    @Mapping(target = "emailChef", source = "emailChef.userEmail")
    @Mapping(target = "restaurant", source = "restaurant.id")
    OrderResponseDto toResponse(OrderModel orderModel);
}
