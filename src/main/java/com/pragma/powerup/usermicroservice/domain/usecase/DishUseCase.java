package com.pragma.powerup.usermicroservice.domain.usecase;


import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotExist;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.model.CategoryAveragePriceModel;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class DishUseCase implements IDishServicePort {
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final UserClient userClient;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IRestaurantServicePort restaurantServicePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, UserClient userClient) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantServicePort = restaurantServicePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userClient = userClient;
    }

    /**
     * Create a restaurant dish
     *
     * @param dishModel - dish information
     * @param idOwner - restaurant owner id
     * @throws UserNotIsOwner - user is not owner
     *
     * @return the dish model with active state and id generated
     * */
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

    /**
     * Updates a dish
     *
     * @param dishModel - dish information
     * @param idOwner - restaurant owner id
     * @throws UserNotIsOwner - user is not owner
     * */
    @Override
    public void updateDish(DishModel dishModel,String idOwner) {
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(dishModel.getRestaurant().getId());

        if (!restaurantModel.getIdOwner().equals(idOwner)) {
            throw new UserNotIsOwner();
        }
        dishPersistencePort.updateDish(dishModel);
    }

    /**
     * Updates a dish state to active or inactive. Checks beforehand if the user is the owner of the dish
     *
     * @param dishModel - dish information
     * @param idOwner - restaurant owner id
     * @throws UserNotIsOwner - user is not owner
     * */

    @Override
    public void updateDishState(DishModel dishModel, String idOwner) {
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(dishModel.getRestaurant().getId());

        if (!restaurantModel.getIdOwner().equals(idOwner)) {
            throw new UserNotIsOwner();
        }

        dishPersistencePort.updateSate(dishModel);
    }

    /**
     * Creates an employee
     *
     * @param  user - employee information
     * @param idRestaurant - restaurant to be linked with the employee
     * @param emailEmployee - email of a employee
     * @throws RestaurantNotExist - The restaurant does not exist
     * */
    @Override
    public RestaurantEmployeeModel createEmployee(User user, Long idRestaurant, String emailEmployee) {
        Optional<RestaurantModel> restaurantModel = Optional.ofNullable(restaurantPersistencePort.getRestaurant(idRestaurant));
        if (restaurantModel.isEmpty()) {
            throw new RestaurantNotExist();
        }
        User createUser = userClient.getUserByEmail(emailEmployee);

        return restaurantEmployeePersistencePort.saveEmployee(
                new RestaurantEmployeeModel(createUser.getEmail(), restaurantModel.get())
        );
    }

    /**
     * List dishes of a specific restaurant, paginated, and grouped by category
     *
     * @param idRestaurant - restaurant id
     * @param page - number of page of the dishes
     * @param elementsXpage - max number of dishes to be returned in the list
     * @throws RestaurantNotExist - The restaurant does not exist
     * */
    @Override
    public List<CategoryWithDishesModel> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage) {
        RestaurantModel restaurantModel = restaurantPersistencePort.getRestaurant(Long.valueOf(idRestaurant));
        if (restaurantModel ==  null) {
            throw new RestaurantNotExist();
        }
        List<DishModel> dishes = dishPersistencePort.listDishesByRestaurant(idRestaurant, page, elementsXpage);

        return dishesGroupByCategory(dishes);
    }

    /**
     * Calculates the average price of all the dishes corresponding to the category they belong to
     *
     * @throws UserNotIsOwner - user is not owner
     * @return List of the categories and the average price of all the dishes that belong to the corresponding category
     */
    @Override
    public List<CategoryAveragePriceModel> calculateAverageByCategory() {
        List<DishModel> dishes = dishPersistencePort.listDishes();
        List<CategoryDishModel> categories = dishPersistencePort.listCategory();

        // Crear la lista de resultados
        List<CategoryAveragePriceModel> results = new ArrayList<>();

        // Iterar por las categorías
        for (CategoryDishModel category : categories) {
            CategoryAveragePriceModel categoryAverage = new CategoryAveragePriceModel();
            categoryAverage.setName(category.getName());

            // Buscar los platos que corresponden a la categoría actual
            List<DishModel> categoryDishes = dishes.stream()
                    .filter(dish -> dish.getCategory().getId().equals(category.getId()))
                    .collect(Collectors.toList());

            // Calcular el promedio para la categoría actual
            double categoryAveragePrice = categoryDishes.stream()
                    .mapToDouble(DishModel::getPrice)
                    .average()
                    .orElse(0.0);

            // Redondear el valor del promedio a dos decimales
            BigDecimal roundedAverage = BigDecimal.valueOf(categoryAveragePrice)
                    .setScale(2, RoundingMode.HALF_UP);

            // Formatear el promedio como un valor monetario con el signo de dólar
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            String formattedAverage = currencyFormatter.format(roundedAverage);

            categoryAverage.setAveragePerDish(formattedAverage);
            results.add(categoryAverage);
        }

        return results;
    }

    /**
     * Groups a list of dishes by category
     *
     * @param dishes - dishes, each dish must have information about its category
     * @return list of categories with the list of dishes it contains
     * */
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
