package com.pragma.powerup.usermicroservice.domain.ports;

import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    void saveOrder(OrderModel orderModel, List<OrdersDishesModel> ordersDishesModelList);
    OrderModel saveOnlyOrder(OrderModel orderModel);
    Integer getNumberOfOrdersWithStateInPreparationPendingOrReady(Long idClient);
    List<OrderWithDishesModel> getOrdersByRestaurantIdAndState(Long restaurantId, int page,
                                                               int elementsXpage, String state);
    Optional<OrderModel> getOrderByRestaurantIdAndOrderId(Long restaurantId, Long orderId);
    OrderWithDishesModel saveOrderToOrderWithDishes(OrderModel orderModel);
    List<OrderModel> getOrdersReadyBySecurityCode(String securityPin);
    Optional<OrderModel> getOrderById(Long orderId);
}
