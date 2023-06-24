package com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl;

import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.CreateEmployeeRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.UpdateDishStateRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryAveragePriceResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryDishesResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.RestaurantEmployeeResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IDishHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.ICreateEmployeeRequestMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishRequestMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IDishResponseMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListDishesAveragePerCategoryMapper;
import com.pragma.powerup.usermicroservice.adapters.driving.http.mapper.IListDishesCategoryByRestaurantMapper;
import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.RestaurantNotExist;
import com.pragma.powerup.usermicroservice.domain.exceptions.SameStateException;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.model.CategoryAveragePriceModel;
import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {
    private final IDishServicePort dishServicePort;
    private final IRestaurantServicePort restaurantServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;
    private final IListDishesCategoryByRestaurantMapper listDishesCategoryByRestaurantMapper;
    private final ICreateEmployeeRequestMapper createEmployeeRequestMapper;
    private final IListDishesAveragePerCategoryMapper dishesAveragePerCategoryMapper;

    /**
     * Creates a new dish
     *
     * @param dishRequestDto - dish information and category name
     * @param idOwner - restaurant owner id
     * @return dish created
     * */
    @Override
    public void saveDish(DishRequestDto dishRequestDto, String idOwner) {
        DishModel dishModel = dishRequestMapper.toDishRequest(dishRequestDto);
        dishModel.setActive(true);
        dishServicePort.saveDish(dishModel, idOwner);
    }

    @Override
    public DishResponseDto getDish(Long id) {
        DishModel dish = dishServicePort.getDish(id);
        return dishResponseMapper.toResponseDish(dish);
    }

    /**
     * Updates a dish
     *
     * @param dishUpdateRequest - information to update with the dish id
     * @param idOwner - restaurant owner id
     * */
    @Override
    public void updateDish(DishUpdateRequest dishUpdateRequest,String idOwner) {
        DishModel dish = dishServicePort.getDish(dishUpdateRequest.getId());

        if(Strings.isNotBlank(dishUpdateRequest.getDescription()) || Strings.isNotEmpty(dishUpdateRequest.getDescription())) dish.setDescription(dishUpdateRequest.getDescription());
        if(dishUpdateRequest.getPrice() > 0) dish.setPrice(dishUpdateRequest.getPrice());

        dishServicePort.updateDish(dish, idOwner);
    }

    /**
     * Updates dish state
     *
     * @param dishUpdateStateRequestDto - dish id and new state
     * @param idOwner - restaurant owner id
     * @throws SameStateException - You must change the state of the plate to a different one
     * */
    @Override
    public void updateState(UpdateDishStateRequestDto dishUpdateStateRequestDto, String idOwner) {
        DishModel dish = dishServicePort.getDish(dishUpdateStateRequestDto.getDishId());

        if (dishUpdateStateRequestDto.isActive() && dish.getActive()) {
            throw new SameStateException();
        }
        dish.setActive(dishUpdateStateRequestDto.isActive());

        dishServicePort.updateDishState(dish, idOwner);
    }

    /**
     * Creates an employee, takes the restaurant of the authenticated owner to create
     * the employee relationship
     *
     * @param createEmployeeRequestDto - employee information
     * @param idRestaurant - restaurant id
     * @param emailEmployee - email employee of the restaurant
     * @return employee email and related restaurant id
     * */
    @Override
    public RestaurantEmployeeResponseDto createEmployee(CreateEmployeeRequestDto createEmployeeRequestDto,
                                                        String idRestaurant,
                                                        String emailEmployee) {
        RestaurantModel restaurant = restaurantServicePort.getRestaurant(Long.valueOf(idRestaurant));
        User userModel = createEmployeeRequestMapper.toUserModel(createEmployeeRequestDto);

        RestaurantEmployeeModel restaurantEmployeeModel = dishServicePort
                .createEmployee(userModel, restaurant.getId(), emailEmployee);

        return createEmployeeRequestMapper.toRestaurantEmployee(restaurantEmployeeModel);
    }

    /**
     * List of dishes of a specific restaurant paginated and grouped by category
     *
     * @param idRestaurant - restaurant id
     * @param page - page number to list
     * @param elementsXpage - elements to show per page
     * @throws RestaurantNotExist - The restaurant does not exist
     * @return list of categories with corresponding dishes
     */
    @Override
    public List<CategoryDishesResponseDto> getDishesCategorizedByRestaurant(String idRestaurant, int page, int elementsXpage) {
        List<CategoryWithDishesModel> categoryWithDishes;

        try {
            categoryWithDishes = dishServicePort.getDishesCategorizedByRestaurant(idRestaurant, page, elementsXpage);
        } catch (RestaurantNotExist e) {
            throw new RestaurantNotExist();
        }
        return categoryWithDishes.stream()
                .map(listDishesCategoryByRestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the average price of all the dishes corresponding to the category they belong to
     *
     * @param idOwner
     * @return List of the categories and the average price of all the dishes that belong to the corresponding category
     */
    @Override
    public List<CategoryAveragePriceResponseDto> calculateAverageByCategory(String idOwner) {
        List<CategoryAveragePriceModel> priceAverage = dishServicePort.calculateAverageByCategory(idOwner);

        return priceAverage.stream()
                .map(dishesAveragePerCategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
