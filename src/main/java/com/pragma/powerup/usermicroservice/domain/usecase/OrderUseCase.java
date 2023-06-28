package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.configuration.Constants;
import com.pragma.powerup.usermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.CancelOrderErrorException;
import com.pragma.powerup.usermicroservice.domain.exceptions.EmployeeNotBelongAnyRestaurant;
import com.pragma.powerup.usermicroservice.domain.exceptions.NoOrdersExceedingTimeLimitException;
import com.pragma.powerup.usermicroservice.domain.exceptions.NotificationNotSend;
import com.pragma.powerup.usermicroservice.domain.exceptions.OrderAssignedOrProcessException;
import com.pragma.powerup.usermicroservice.domain.exceptions.OrderNotExist;
import com.pragma.powerup.usermicroservice.domain.exceptions.OrderStateCannotChange;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotHaveTheseDishes;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserHaveOrderException;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantPendingDeleteException;
import com.pragma.powerup.usermicroservice.domain.exceptions.SecurityCodeIncorrectException;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
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
    public OrderModel getOrder(Long id) {
        return orderPersistencePort.getOrder(id);
    }

    /**
     * Create a new order verifying first the client doesn't have one already in process
     *
     * @param ordersDishesModels - dishes the client wants
     * @param idRestaurant - the restaurant the dishes belongs to
     * @param idClient - client id
     * @throws UserHaveOrderException - the client already has an order in process
     * @throws RestaurantNotHaveTheseDishes - These dishes do not belong to this restaurant
     * 
     * */

    @Override
    public void newOrder(String idRestaurant, String idClient, List<OrdersDishesModel> ordersDishesModels) {

        Integer orderWithStatePendingPreparingOrReady = orderPersistencePort.getNumberOfOrdersWithStateInPreparationPendingOrReady(Long.valueOf(idClient));
        if (orderWithStatePendingPreparingOrReady != null && orderWithStatePendingPreparingOrReady > 0) {
            throw new UserHaveOrderException();
        }
        RestaurantModel restaurantModel = restaurantPersistencePort.getRestaurant(Long.valueOf(idRestaurant));
        if (restaurantModel.getState().equals("PENDING_DELETE")) {
            throw new RestaurantPendingDeleteException();
        }

        OrderModel orderModel = new OrderModel();
        orderModel.setRestaurant(restaurantModel);
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

    /**
     * Gets the orders from employee's restaurant filtered by the specified state
     *
     * @param orderState - Must be already validated
     * @param page - page number to show. For pagination
     * @param elementsXpage - elements to show per page. For pagination
     * @param employeeEmail - employee email
     * @throws EmployeeNotBelongAnyRestaurant - Employee does not belong to any restaurant
     * @return list of orders with its dishes
     * */
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

    /**
     * Assigns a pending order to an employee and changes its state to in preparation
     *
     * @param orderId - order to find
     * @param employeeEmail - employee email to assign to the order
     * @throws EmployeeNotBelongAnyRestaurant - employee doesn't have a restaurant assigned
     * @throws OrderNotExist - order with the id specified couldn't be found
     * @throws OrderAssignedOrProcessException - Order is already assigned or in process
     * */
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

    /**
     * Change order to ready state, and notifies the client with a pin code generated
     *
     * @param orderId - order id corresponding to the order to change
     * @param employeeEmail - employee email
     * @throws EmployeeNotBelongAnyRestaurant - employee doesn't have a restaurant assigned
     * @throws OrderNotExist - order with the id specified couldn't be found
     * @throws OrderStateCannotChange - The status of this order cannot be changed
     * @throws NotificationNotSend - Notification could not be sent
     * @return the order updated
     * */
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

        User user = userClient.getClientByEmployee(orderModel.get().getIdClient());
        String messageToSend = Constants.MESSAGE_TO_CLIENT + orderModel.get().getSecurityPin();
        boolean notificationMade = messagingClient.notifyClient(messageToSend, user.getPhone());

        if (!notificationMade) {
            throw new NotificationNotSend();
        }

        return saveOrder;
    }

    /**
     * Changes the order to 'delivered', verifies the security code is correct
     *
     * @param employeeEmail employee email
     * @param orderId order id
     * @param securityCode security code to receive the order
     * @throws EmployeeNotBelongAnyRestaurant - employee doesn't have a restaurant assigned
     * @throws OrderNotExist - order with the id specified couldn't be found
     * @throws SecurityCodeIncorrectException - Wrong security code
     * @throws OrderStateCannotChange - The status of this order cannot be changed
     * @return the order updated
     * */
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

    /**
     * Cancel an order with its id and has to belong the client email
     *
     * @param clientEmail client email
     * @param orderId order id to be cancelled
     * @throws OrderNotExist - order with the id specified couldn't be found
     * @throws CancelOrderErrorException - Sorry, your order is already being prepared and cannot be canceled
     * */
    @Override
    public void cancelOrder(String clientEmail, Long orderId) {
        Optional<OrderModel> orderModel = orderPersistencePort.getOrderById(orderId);
        User client = userClient.getClient(orderModel.get().getIdClient());
        if (orderModel.isEmpty() || !Objects.equals(client.getEmail(), clientEmail)) {
            throw new OrderNotExist();
        }
        if (!orderModel.get().getState().equals(Constants.ORDER_PENDING_STATE)){
            messagingClient.notifyClient(Constants.MESSAGE_CANCEL_ORDER, client.getPhone());
            throw new CancelOrderErrorException();
        }

        orderModel.get().setState(Constants.ORDER_CANCELED_STATE);
        orderPersistencePort.saveOnlyOrder(orderModel.get());
    }

    /**
     * Cancel orders after a specific time
     *
     * @param timeLimit
     * @throws NoOrdersExceedingTimeLimitException - No order exceeds the time limit
     */
    @Override
    public void cancelOrderByWaitingTime(int timeLimit) {
        List<OrderModel> orders = orderPersistencePort.getAllOrders();
        boolean orderExceededTimeLimit = false;

        for (OrderModel order : orders) {
            if (order.getState().equals(Constants.ORDER_CANCELED_STATE)) {
                // Ignorar las órdenes que tienen el estado "CANCELADO"
                continue;
            }

            if (exceedsWaitingTime(order, timeLimit)) {
                order.setState(Constants.ORDER_CANCELED_STATE);
                orderPersistencePort.saveOnlyOrder(order);
                User user = userClient.getClientByOwner(order.getIdClient());
                String messageToSend = Constants.ORDER_CANCELED_MESSAGE_TO_USER;
                messagingClient.notifyClient(messageToSend, user.getPhone());
                orderExceededTimeLimit = true;
            }
        }

        if (!orderExceededTimeLimit) {
            throw new NoOrdersExceedingTimeLimitException();
        }
    }

    /**
     * Checks if an order has exceeded the given time limit
     *
     * @param order
     * @param time
     * @return boolean
     */
    private boolean exceedsWaitingTime(OrderModel order, int time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeLimit = order.getDate().plusMinutes(time);
        return now.isAfter(timeLimit);
    }
    /**
     * Generates a PIN Code of 6 digits randomly
     *
     * @return generatedPinCode
     * */
    private String generatedPinCode() {
        Random rand = new Random();

        int n = rand.nextInt(1000000);
        return String.format("%06d", n);
    }
}
