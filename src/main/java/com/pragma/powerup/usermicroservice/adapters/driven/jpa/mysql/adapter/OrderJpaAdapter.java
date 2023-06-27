package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.client.TraceabilityClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.Trace;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderStateType;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrdersDishesEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IOrderDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IOrderEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IOrderDishRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IOrderRepository;
import com.pragma.powerup.usermicroservice.configuration.Constants;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IOrderDishRepository orderDishRepository;
    private final IOrderDishEntityMapper orderDishEntityMapper;
    private final TraceabilityClient traceClient;

    @Override
    public OrderModel getOrder(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).get();
        return orderEntityMapper.toModel(orderEntity);
    }

    @Override
    public void saveOrder(OrderModel orderModel, List<OrdersDishesModel> ordersDishesModelList) {

        OrderEntity orderEntity = orderEntityMapper.toEntityOrder(orderModel);
        orderRepository.save(orderEntity);

        List<OrdersDishesEntity> ordersDishesEntities = ordersDishesModelList.stream()
                .map(orderDishEntityMapper::toEntity)
                .peek(orderDishEntity -> orderDishEntity.setOrder(orderEntity))
                .collect(Collectors.toList());

//      createLoggOrder(orderEntity, "", Constants.ORDER_PENDING_STATE);
        orderDishRepository.saveAll(ordersDishesEntities);
    }

    @Override
    public OrderModel saveOnlyOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.toEntityOrder(orderModel);
        return orderEntityMapper.toModel(orderRepository.save(orderEntity));
    }

    @Override
    public Integer getNumberOfOrdersWithStateInPreparationPendingOrReady(Long idClient) {
        return orderRepository.getNumberOfOrdersWithStateInPreparationPendingOrReady(idClient);
    }

    @Override
    public List<OrderWithDishesModel> getOrdersByRestaurantIdAndState(Long restaurantId,
                                                                      int page,
                                                                      int elementsXpage,
                                                                      String state) {
        OrderStateType orderStateType = convertStringToOrderStateType(state);
        PageRequest pageable = PageRequest.of(page, elementsXpage);

        List<OrderEntity> orderEntities = orderRepository.findByRestaurantIdAndState(restaurantId,
                orderStateType,
                pageable);
        return orderEntities.stream()
                .map(orderEntityMapper::toOrderWithDishesModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderModel> getOrderByRestaurantIdAndOrderId(Long restaurantId, Long orderId) {
        OrderEntity orderEntity = orderRepository
                .findByRestaurantIdAndId(restaurantId, orderId).orElse(null);
        return Optional.ofNullable(orderEntityMapper.toModel(orderEntity));
    }

    @Override
    public OrderWithDishesModel saveOrderToOrderWithDishes(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.toEntityOrder(orderModel);
        return orderEntityMapper.toOrderWithDishesModel(orderRepository.save(orderEntity));
    }

    @Override
    public List<OrderModel> getOrdersReadyBySecurityCode(String securityPin) {
        List<OrderEntity> orderEntities = orderRepository
                .findBySecurityPinAndState(securityPin, OrderStateType.LISTO);
        return orderEntities.stream()
                .map(orderEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderModel> getOrderById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).get();
        return Optional.ofNullable(orderEntityMapper.toModel(orderEntity));
    }

    @Override
    public List<OrderModel> getOrdersByRestaurantId(Long id) {
        List<OrderEntity> orderEntities = orderRepository.getOrdersByRestaurantId(id);

        return orderEntities.stream()
                .map(orderEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderStateType convertStringToOrderStateType(String state) {
        OrderStateType stateToProcess;
        switch (state) {
            case Constants.ORDER_PENDING_STATE:
                stateToProcess = OrderStateType.PENDIENTE;
                break;
            case Constants.ORDER_CANCELED_STATE:
                stateToProcess = OrderStateType.CANCELADO;
                break;
            case Constants.ORDER_DELIVERED_STATE:
                stateToProcess = OrderStateType.ENTREGADO;
                break;
            case Constants.ORDER_READY_STATE:
                stateToProcess = OrderStateType.LISTO;
                break;
            default:
                stateToProcess = OrderStateType.EN_PREPARACION;
                break;
        }
        return stateToProcess;
    }

    private void createLoggOrder(OrderEntity order, String previousStatus, String currentStatus){
        Trace trace = initializeTracking(order, previousStatus, currentStatus);
        traceClient.saveTrace(trace);
    }

    private Trace initializeTracking(OrderEntity order, String previousStatus, String currentStatus){
        Trace trace = new Trace();
        trace.setOrderId(String.valueOf(order.getId()));
        trace.setEmployeeEmail(String.valueOf(order.getEmailChef()));
        trace.setClientId(String.valueOf(order.getIdClient()));
        trace.setPreviousState(previousStatus);
        trace.setNewState(currentStatus);
        return trace;
    }
}
