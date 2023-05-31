package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.configuration.Constants;
import com.pragma.powerup.usermicroservice.domain.api.IClientServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotExist;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotHaveTheseDishes;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.UserHaveOrderException;
import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.OrderModel;
import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientUseCase implements IClientServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;

    public ClientUseCase( IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IOrderPersistencePort orderPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
    }

    @Override
    public List<RestaurantModel> listRestaurant(int page, int numberOfElements) {
        return restaurantPersistencePort.listByPageAndElements(page, numberOfElements);
    }

    @Override
    public List<CategoryWithDishesModel> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage) {
        RestaurantModel restaurantModel = restaurantPersistencePort.getRestaurant(Long.valueOf(idRestaurant));
        if (restaurantModel ==  null) {
            throw new RestaurantNotExist();
        }
        List<DishModel> dishes = dishPersistencePort.listDishesByRestaurant(idRestaurant, page, elementsXpage);

        return dishesGroupByCategory(dishes);
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

    private List<CategoryWithDishesModel> dishesGroupByCategory(List<DishModel> dishes) {
        Map<Long, List<DishModel>> dishesMap = dishes.stream()
                .collect(Collectors.groupingBy(dishModel -> dishModel.getCategory().getId(),
                        Collectors.mapping(dishModel -> dishModel, Collectors.toList())));

        List<CategoryWithDishesModel> categories = new ArrayList<>();
        dishesMap.forEach((categoryid, dishModels) -> {
            CategoryWithDishesModel category = new CategoryWithDishesModel();
            category.setId(dishModels.get(0).getCategory().getId());
            category.setName(dishModels.get(0).getCategory().getName());
            category.setDescription(dishModels.get(0).getCategory().getDescription());
            category.setDishes(dishModels);
            categories.add(category);
                });
        return categories;
    }
}
