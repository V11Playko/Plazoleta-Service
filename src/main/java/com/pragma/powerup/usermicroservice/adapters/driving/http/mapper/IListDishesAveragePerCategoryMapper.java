package com.pragma.powerup.usermicroservice.adapters.driving.http.mapper;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryAveragePriceResponseDto;
import com.pragma.powerup.usermicroservice.domain.model.CategoryAveragePriceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IListDishesAveragePerCategoryMapper {
    @Mapping(target = "name", source = "name")
    @Mapping(target = "averagePerDish", source = "averagePerDish")
    CategoryAveragePriceResponseDto toDto(CategoryAveragePriceModel category);
}
