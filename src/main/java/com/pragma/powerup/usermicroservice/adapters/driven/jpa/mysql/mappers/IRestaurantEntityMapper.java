package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantEntityMapper {
    RestaurantEntity toEntityRestaurant(RestaurantModel restaurantModel);
    RestaurantModel toRestaurantModel(RestaurantEntity restaurantEntity);

    @Named("toModelNoDishes")
    @Mapping(target = "dishes", ignore = true)
    RestaurantModel toModelNoDishes(RestaurantEntity restaurantEntity);
}
