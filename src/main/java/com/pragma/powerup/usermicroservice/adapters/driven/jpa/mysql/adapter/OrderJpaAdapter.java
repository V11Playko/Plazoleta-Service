package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrdersDishesEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IOrderDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IOrderEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IOrderDishRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IOrderRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserHaveOrderException;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IOrderDishRepository orderDishRepository;
    private final IOrderDishEntityMapper orderDishEntityMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveOrder(OrderModel orderModel, List<OrdersDishesModel> ordersDishesModelList) {
        Long countPedidosPendientes = (Long) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM orders "
                        + "WHERE (state = 'PENDING' OR state = 'PREPARATION' OR state = 'READY') "
                        + "AND client_id = :idClient "
                        + "AND state IS NOT NULL")
                .setParameter("idClient", orderModel.getIdClient())
                .getSingleResult();

        // Verificar si hay pedidos pendientes
        if (countPedidosPendientes > 0) {
            throw new UserHaveOrderException();
        }

        OrderEntity orderEntity = orderEntityMapper.toEntityOrder(orderModel);
        orderRepository.save(orderEntity);

        List<OrdersDishesEntity> ordersDishesEntities = ordersDishesModelList.stream()
                .map(orderDishEntityMapper::toEntity)
                .peek(orderDishEntity -> orderDishEntity.setOrder(orderEntity))
                .collect(Collectors.toList());

        orderDishRepository.saveAll(ordersDishesEntities);
    }

    @Override
    public Integer getNumberOfOrdersWithStateInPreparationPendingOrReady(Long idClient) {
        return orderRepository.getNumberOfOrdersWithStateInPreparationPendingOrReady(idClient);
    }
}
