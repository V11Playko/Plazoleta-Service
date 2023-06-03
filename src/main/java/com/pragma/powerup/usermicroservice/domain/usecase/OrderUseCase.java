package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.configuration.Constants;
import com.pragma.powerup.usermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.DomainException;
import com.pragma.powerup.usermicroservice.domain.exceptions.EmployeeNotBelongAnyRestaurant;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotHaveTheseDishes;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserHaveOrderException;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OrderUseCase implements IOrderServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    public OrderUseCase(IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IOrderPersistencePort orderPersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort ) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
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
}
