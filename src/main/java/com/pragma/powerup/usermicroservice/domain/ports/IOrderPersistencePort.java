package com.pragma.powerup.usermicroservice.domain.ports;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;

public interface IOrderPersistencePort {
    OrderEntity saveOrder(OrderModel orderModel);
}
