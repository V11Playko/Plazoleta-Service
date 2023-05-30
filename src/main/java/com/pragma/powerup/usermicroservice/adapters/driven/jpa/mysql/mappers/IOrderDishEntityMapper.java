package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrdersDishesEntity;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = { IOrderEntityMapper.class, IDishEntityMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderDishEntityMapper {

    @Mapping(target = "order", source = "order")
    @Mapping(target = "dish", source = "dish")
    @Mapping(target = "amount", source = "amount")
    OrdersDishesEntity toEntity(OrdersDishesModel ordersDishesModel);
}
