package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Named("dishEntityMapper")
@Mapper(componentModel = "spring",
        uses = IRestaurantEntityMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishEntityMapper {
    DishEntity toEntityDish(DishModel dishModel);
    @Named("toModel")
    DishModel toDishModel(DishEntity dishEntity);
}
