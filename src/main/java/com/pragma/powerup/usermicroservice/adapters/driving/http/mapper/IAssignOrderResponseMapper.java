package com.pragma.powerup.usermicroservice.adapters.driving.http.mapper;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.AssignOrderResponseDto;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAssignOrderResponseMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "clientEmail", source = "client")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "chefEmail", source = "chef")
    AssignOrderResponseDto toDto(OrderWithDishesModel order);
}
