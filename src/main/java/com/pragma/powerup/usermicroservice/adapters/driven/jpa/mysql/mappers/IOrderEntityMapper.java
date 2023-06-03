package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderStateType;
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
    @Mapping(target = "restaurant", qualifiedByName = {"restaurantEntityMapper"})
    @Mapping(target = "emailChef", source = "emailChef", qualifiedByName = {"restaurantEmployeeEntityMapper", "toModel"})
    @Mapping(target = "state", source = "state", qualifiedByName = "stateEnumToString")
    OrderModel toModel(OrderEntity orderEntity);

    @Named("toEntity")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "idClient", source = "idClient")
    @Mapping(target = "state", source = "state", qualifiedByName = "stateStringToEnum")
    @Mapping(target = "restaurant", source = "restaurant")
    @Mapping(target = "emailChef", source = "emailChef")
    OrderEntity toEntityOrder(OrderModel orderModel);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "client", source = "idClient")
    @Mapping(target = "state", source = "state", qualifiedByName = "stateEnumToString")
    @Mapping(target = "chef", source = "emailChef.userEmail")
    @Mapping(target = "orderDishes", source = "orderDish", qualifiedByName = {"toOrderDishModel"})
    OrderWithDishesModel toOrderWithDishesModel(OrderEntity orderEntity);

    @Named("toOrderDishModel")
    @Mapping(target = "dish", source = "dish", qualifiedByName = {"dishEntityMapper", "toModel"})
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "amount", source = "amount")
    OrdersDishesModel toOrderDishModel(OrdersDishesEntity orderDishEntity);

    @Named("stateStringToEnum")
    static OrderStateType stateStringToEnum(String stateString) {
        switch (stateString) {
            case "PENDING":
                return OrderStateType.PENDIENTE;
            case "PREPARATION":
                return OrderStateType.EN_PREPARACION;
            case "CANCELED":
                return OrderStateType.CANCELADO;
            case "READY":
                return OrderStateType.LISTO;
            case "DELIVERED":
                return OrderStateType.ENTREGADO;
            default:
                return OrderStateType.CANCELADO;
        }
    }

    @Named("stateEnumToString")
    static String stateEnumToString(OrderStateType orderStateType) {
        switch (orderStateType) {
            case PENDIENTE:
                return "PENDING";
            case EN_PREPARACION:
                return "PREPARATION";
            case CANCELADO:
                return "CANCELED";
            case LISTO:
                return "READY";
            case ENTREGADO:
                return "DELIVERED";
            default:
                return "CANCELED";
        }
    }
}
