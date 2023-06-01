package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrdersDishesEntity;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = { IOrderEntityMapper.class, IDishEntityMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderDishEntityMapper {

    @Mapping(target = "order", source = "order", qualifiedByName = {"orderEntityMapper", "toEntity"})
    @Mapping(target = "dish", source = "dish")
    @Mapping(target = "amount", source = "amount")
    OrdersDishesEntity toEntity(OrdersDishesModel ordersDishesModel);

    @Mapping(target = "dish", source = "dish", qualifiedByName = {"dishEntityMapper", "toModel"})
    @Mapping(target = "order", source = "order", qualifiedByName = {"orderEntityMapper", "toModel"})
    @Mapping(target = "amount", source = "amount")
    OrdersDishesModel toModel(OrdersDishesEntity orderDishEntity);
}
