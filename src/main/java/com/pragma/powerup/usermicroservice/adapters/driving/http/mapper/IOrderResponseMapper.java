package com.pragma.powerup.usermicroservice.adapters.driving.http.mapper;


import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {
    @Mapping(target = "restaurant", source = "restaurant.id")
    @Mapping(target = "idOrder", source = "idOrder.idOrder")
    OrderResponseDto toResponseOrder(OrderModel orderModel);
}
