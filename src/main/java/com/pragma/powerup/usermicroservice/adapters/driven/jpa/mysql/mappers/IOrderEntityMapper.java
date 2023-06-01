package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.EOrderStateType;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrdersDishesEntity;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Named("orderEntityMapper")
@Mapper(componentModel = "spring",
        uses = { IRestaurantEntityMapper.class, IDishEntityMapper.class, IRestaurantEmployeeEntityMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {
    @Named("toModel")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "idClient", source = "idClient")
    @Mapping(target = "restaurant", qualifiedByName = {"restaurantEntityMapper", "toModelNoDishes"})
    @Mapping(target = "idChef", source = "idChef", qualifiedByName = {"restaurantEmployeeEntityMapper", "toModel"})
    @Mapping(target = "state", source = "state", qualifiedByName = "stateEnumToString")
    OrderModel toModel(OrderEntity orderEntity);

    @Named("toEntity")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "idClient", source = "idClient")
    @Mapping(target = "state", source = "state", qualifiedByName = "stateStringToEnum")
    @Mapping(target = "restaurant", source = "restaurant")
    @Mapping(target = "idChef", source = "idChef")
    OrderEntity toEntityOrder(OrderModel orderModel);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "client", source = "idClient")
    @Mapping(target = "state", source = "state", qualifiedByName = "stateEnumToString")
    @Mapping(target = "chef", source = "idChef.userEmail")
    @Mapping(target = "orderDishes", source = "orderDish", qualifiedByName = {"toOrderDishModel"})
    OrderWithDishesModel toOrderWithDishesModel(OrderEntity orderEntity);

    @Named("toOrderDishModel")
    @Mapping(target = "dish", source = "dish", qualifiedByName = {"dishEntityMapper", "toModel"})
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "amount", source = "amount")
    OrdersDishesModel toOrderDishModel(OrdersDishesEntity orderDishEntity);

    @Named("stateStringToEnum")
    static EOrderStateType stateStringToEnum(String stateString) {
        switch (stateString) {
            case "PENDING":
                return EOrderStateType.PENDING;
            case "PREPARATION":
                return EOrderStateType.PREPARATION;
            case "CANCELED":
                return EOrderStateType.CANCELED;
            case "READY":
                return EOrderStateType.READY;
            case "DELIVERED":
                return EOrderStateType.DELIVERED;
            default:
                return EOrderStateType.CANCELED;
        }
    }

    @Named("stateEnumToString")
    static String stateEnumToString(EOrderStateType orderStateType) {
        switch (orderStateType) {
            case PENDING:
                return "PENDING";
            case PREPARATION:
                return "PREPARATION";
            case CANCELED:
                return "CANCELED";
            case READY:
                return "READY";
            case DELIVERED:
                return "DELIVERED";
            default:
                return "CANCELED";
        }
    }
}
