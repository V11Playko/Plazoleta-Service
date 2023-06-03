package com.pragma.powerup.usermicroservice.adapters.driving.http.mapper;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IListOrdersResponseMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "client", source = "client")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "chef", source = "chef")
    @Mapping(target = "orderDishes", source = "orderDishes", qualifiedByName = { "toDishResponseDto" })
    OrderResponseDto toDto(OrderWithDishesModel order);

    @Named("toDishResponseDto")
    @Mapping(target = "id", source = "dish.id")
    @Mapping(target = "name", source = "dish.name")
    @Mapping(target = "description", source = "dish.description")
    @Mapping(target = "price", source = "dish.price")
    @Mapping(target = "urlImage", source = "dish.urlImage")
    @Mapping(target = "active", source = "dish.active")
    DishResponseDto toDishResponseDto(OrdersDishesModel orderDishModel);
}
