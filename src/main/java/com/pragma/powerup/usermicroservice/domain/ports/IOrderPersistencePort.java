package com.pragma.powerup.usermicroservice.domain.ports;

import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;

import java.util.List;

public interface IOrderPersistencePort {
    void saveOrder(OrderModel orderModel, List<OrdersDishesModel> ordersDishesModelList);
    Integer getNumberOfOrdersWithStateInPreparationPendingOrReady(Long idClient);
}
