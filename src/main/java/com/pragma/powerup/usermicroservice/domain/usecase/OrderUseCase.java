package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.configuration.Constants;
import com.pragma.powerup.usermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.EmployeeNotBelongAnyRestaurant;
import com.pragma.powerup.usermicroservice.domain.exceptions.NotificationNotSend;
import com.pragma.powerup.usermicroservice.domain.exceptions.OrderAssignedOrProcessException;
import com.pragma.powerup.usermicroservice.domain.exceptions.OrderNotExist;
import com.pragma.powerup.usermicroservice.domain.exceptions.OrderStateCannotChange;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotHaveTheseDishes;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserHaveOrderException;
import com.pragma.powerup.usermicroservice.domain.exceptions.SecurityCodeIncorrectException;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IMessagingPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class OrderUseCase implements IOrderServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IMessagingPersistencePort messagingClient;
    private final UserClient userClient;

    public OrderUseCase(IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IOrderPersistencePort orderPersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, UserClient userClient, IMessagingPersistencePort messagingClient1) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userClient = userClient;
        this.messagingClient = messagingClient1;
    }
    @Override
    public void newOrder(String idRestaurant, String idClient, List<OrdersDishesModel> ordersDishesModels) {

        Integer orderWithStatePendingPreparingOrReady = orderPersistencePort.getNumberOfOrdersWithStateInPreparationPendingOrReady(Long.valueOf(idClient));
        if (orderWithStatePendingPreparingOrReady != null && orderWithStatePendingPreparingOrReady > 0) {
            throw new UserHaveOrderException();
        }

        OrderModel orderModel = new OrderModel();
        orderModel.setRestaurant(restaurantPersistencePort.getRestaurant(Long.valueOf(idRestaurant)));
        orderModel.setIdClient(idClient);
        orderModel.setDate(LocalDateTime.now());
        orderModel.setState(Constants.ORDER_PENDING_STATE);

        for (OrdersDishesModel dishesModel : ordersDishesModels) {
            DishModel dishModel = dishPersistencePort.getDish(dishesModel.getDish().getId());

            if (!dishModel.getRestaurant().getId().equals(Long.valueOf(idRestaurant))) {
                throw new RestaurantNotHaveTheseDishes();
            }
            dishesModel.setDish(dishModel);
            dishesModel.setOrder(orderModel);
        }

        orderPersistencePort.saveOrder(orderModel, ordersDishesModels);
    }

    @Override
    public List<OrderWithDishesModel> listOrdersByState(String orderState, int page, int elementsXpage, String employeeEmail) {
        Optional<RestaurantEmployeeModel> employeeModel = restaurantEmployeePersistencePort
                .findByEmployeeEmail(employeeEmail);
        if (employeeModel.isEmpty()) {
            throw new EmployeeNotBelongAnyRestaurant();
        }
        return orderPersistencePort
                .getOrdersByRestaurantIdAndState(employeeModel.get().getRestaurant().getId(),
                page, elementsXpage, orderState);
    }

    @Override
    public OrderWithDishesModel assignOrder(String employeeEmail, Long orderId) {

        Optional<RestaurantEmployeeModel> employeeModel = restaurantEmployeePersistencePort
                .findByEmployeeEmail(employeeEmail);
        if (employeeModel.isEmpty()) {
            throw new EmployeeNotBelongAnyRestaurant();
        }

        Optional<OrderModel> orderModel = orderPersistencePort
                .getOrderByRestaurantIdAndOrderId(employeeModel.get().getRestaurant().getId(), orderId);
        if (orderModel.isEmpty()) {
            throw new OrderNotExist();
        }

        if (!Objects.equals(orderModel.get().getState(), Constants.ORDER_PENDING_STATE) ||
            orderModel.get().getEmailChef() != null) {
            throw new OrderAssignedOrProcessException();
        }

        orderModel.get().setEmailChef(employeeModel.get());
        orderModel.get().setState(Constants.ORDER_PREPARATION_STATE);

        return  orderPersistencePort.saveOrderToOrderWithDishes(orderModel.get());
    }

    @Override
    public OrderModel changeOrderToReady(String employeeEmail, Long orderId) {
        List<OrderModel> foundOrders;
        String pinGenerated;

        Optional<RestaurantEmployeeModel> employeeModel = restaurantEmployeePersistencePort
                .findByEmployeeEmail(employeeEmail);
        if (employeeModel.isEmpty()) {
            throw new EmployeeNotBelongAnyRestaurant();
        }

        Optional<OrderModel> orderModel = orderPersistencePort
                .getOrderByRestaurantIdAndOrderId(employeeModel.get().getRestaurant().getId(), orderId);
        if (orderModel.isEmpty()) {
            throw new OrderNotExist();
        }

        if (!orderModel.get().getState().equals(Constants.ORDER_PREPARATION_STATE)) {
            throw new OrderStateCannotChange();
        }

        do {
            pinGenerated = generatedPinCode();
            foundOrders = orderPersistencePort.getOrdersReadyBySecurityCode(pinGenerated);
        } while (!foundOrders.isEmpty());
        orderModel.get().setState(Constants.ORDER_READY_STATE);
        orderModel.get().setSecurityPin(pinGenerated);
        OrderModel saveOrder = orderPersistencePort.saveOnlyOrder(orderModel.get());

        User user = userClient.getClient(orderModel.get().getIdClient());
        String messageToSend = Constants.MESSAGE_TO_CLIENT + orderModel.get().getSecurityPin();
        boolean notificationMade = messagingClient.notifyClient(messageToSend, user.getPhone());

        if (!notificationMade) {
            throw new NotificationNotSend();
        }

        return saveOrder;
    }

    @Override
    public OrderModel changeOrderToDelivered(String employeeEmail, Long orderId, String securityCode) {
        Optional<RestaurantEmployeeModel> employeeModel = restaurantEmployeePersistencePort
                .findByEmployeeEmail(employeeEmail);
        if (employeeModel.isEmpty()) {
            throw new EmployeeNotBelongAnyRestaurant();
        }

        Optional<OrderModel> orderModel = orderPersistencePort
                .getOrderByRestaurantIdAndOrderId(employeeModel.get().getRestaurant().getId(), orderId);
        if (orderModel.isEmpty()) {
            throw new OrderNotExist();
        }
        if (orderModel.get().getSecurityPin() == null || !orderModel.get().getSecurityPin().equals(securityCode)) {
            throw new SecurityCodeIncorrectException();
        }
        if (orderModel.get().getState() == null || !orderModel.get().getState().equals(Constants.ORDER_READY_STATE)){
            throw new OrderStateCannotChange();
        }


        orderModel.get().setState(Constants.ORDER_DELIVERED_STATE);
        return orderPersistencePort.saveOnlyOrder(orderModel.get());
    }


    private String generatedPinCode() {
        Random rand = new Random();

        int n = rand.nextInt(1000000);
        return String.format("%06d", n);
    }
}
