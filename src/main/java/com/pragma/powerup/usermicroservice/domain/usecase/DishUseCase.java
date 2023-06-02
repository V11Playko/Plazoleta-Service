package com.pragma.powerup.usermicroservice.domain.usecase;


import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotExist;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class DishUseCase implements IDishServicePort {
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IRestaurantServicePort restaurantServicePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IRestaurantServicePort restaurantServicePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantServicePort = restaurantServicePort;
    }

    @Override
    public void saveDish(DishModel dishModel, String idOwner) {

        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(dishModel.getRestaurant().getId());

            if (!restaurantModel.getIdOwner().equals(idOwner)) {
                throw new UserNotIsOwner();
            }
            dishPersistencePort.saveDish(dishModel);
    }

    @Override
    public DishModel getDish(Long id) {
        return dishPersistencePort.getDish(id);
    }

    @Override
    public void updateDish(DishModel dishModel,String idOwner) {
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(dishModel.getRestaurant().getId());

        if (!restaurantModel.getIdOwner().equals(idOwner)) {
            throw new UserNotIsOwner();
        }
        dishPersistencePort.updateDish(dishModel);
    }

    @Override
    public void updateDishState(DishModel dishModel, String idOwner) {
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(dishModel.getRestaurant().getId());

        if (!restaurantModel.getIdOwner().equals(idOwner)) {
            throw new UserNotIsOwner();
        }

        dishPersistencePort.updateSate(dishModel);
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
