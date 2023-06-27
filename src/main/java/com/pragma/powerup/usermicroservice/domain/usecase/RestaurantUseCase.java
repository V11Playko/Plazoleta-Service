package com.pragma.powerup.usermicroservice.domain.usecase;


import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.configuration.Constants;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantHaveOrdersPending;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantPendingDeleteException;
import com.pragma.powerup.usermicroservice.domain.exceptions.ValidateRoleOwner;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import jakarta.transaction.Transactional;

import java.util.List;


public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final UserClient userClient;
    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IOrderPersistencePort orderPersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, UserClient userClient) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userClient = userClient;
    }

    /**
     * Creates a restaurant, validating first if the user id correspond to a
     * restaurant owner in the user service
     *
     * @param restaurantModel - restaurant information
     * @throws ValidateRoleOwner - Role is not owned by an owner
     *
     * */
    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        //Traer usuario con el usecase
        restaurantModel.setState("READY");
        try {
            User user = userClient.getUser(restaurantModel.getIdOwner());
            if (user.getIdRole().equals("2")) {
                restaurantPersistencePort.saveRestaurant(restaurantModel);
            }
        } catch (RuntimeException e) {
            throw new ValidateRoleOwner();
        }

    }

    @Override
    public RestaurantModel getRestaurant(Long id) {
        return restaurantPersistencePort.getRestaurant(id);
    }

    /**
     * List restaurants
     *
     * @param page - number of the page of the restaurants
     * @param numberOfElements - max number of restaurants to be returned in the list
     * @return list of the restaurants sorted alphabetically
     * */
    @Override
    public List<RestaurantModel> listRestaurant(int page, int numberOfElements) {
        return restaurantPersistencePort.listByPageAndElements(page, numberOfElements);
    }

    /**
     *  Delete restaurant and its associated parts
     * @param id
     * @throws  RestaurantHaveOrdersPending - An exception is thrown if the restaurant has orders in status "PENDING"
     */
    @Override
    public void deleteRestaurant(Long id) {
        RestaurantModel restaurant = restaurantPersistencePort.getRestaurant(id);

        if (restaurant.getState().equals(Constants.PENDING_DELETE)) {
            throw new RestaurantPendingDeleteException();
        }

        Integer orderWithStatePending = restaurantPersistencePort.getNumberOfOrdersWithStateInPending(id);
        if (orderWithStatePending != null && orderWithStatePending > 0) {
            restaurant.setState(Constants.PENDING_DELETE);
            restaurantPersistencePort.updateRestaurant(restaurant);
            throw new RestaurantHaveOrdersPending();
        }

        deleteAll(id);
    }

    /**
     * Method responsible for removing all parts associated with a restaurant
     *
     * @param id
     */
    @Transactional
    private void deleteAll(Long id) {
        // Eliminar los platos (Dish)
        List<DishModel> dishes = dishPersistencePort.getDishesByRestaurantId(id);
        for (DishModel dish : dishes) {
            dishPersistencePort.deleteDishById(dish.getId());
        }

        // Eliminar los pedidos (Orders)
        List<OrderModel> orders = orderPersistencePort.getOrdersByRestaurantId(id);
        for (OrderModel order : orders) {
            orderPersistencePort.deleteOrderById(order.getId());
        }

        // Eliminar los empleados del restaurante por correo
        List<RestaurantEmployeeModel> employees = restaurantEmployeePersistencePort.getEmployeesByRestaurantId(id);
        for (RestaurantEmployeeModel employee : employees) {
            restaurantEmployeePersistencePort.deleteEmployeeByEmailAndRestaurantId(employee.getUserEmail(), id);
        }

        restaurantPersistencePort.deleteRestaurantById(id);
    }
}
