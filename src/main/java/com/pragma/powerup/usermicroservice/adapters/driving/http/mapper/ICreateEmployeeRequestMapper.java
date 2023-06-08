package com.pragma.powerup.usermicroservice.adapters.driving.http.mapper;

import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.CreateEmployeeRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.RestaurantEmployeeResponseDto;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICreateEmployeeRequestMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    User toUserModel(CreateEmployeeRequestDto createEmployeeRequestDto);

    @Mapping(source = "userEmail", target = "email")
    @Mapping(source = "restaurant.id", target = "restaurantId")
    RestaurantEmployeeResponseDto toRestaurantEmployee(RestaurantEmployeeModel restaurantEmployeeModel);
}
