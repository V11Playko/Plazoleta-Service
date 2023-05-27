package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.CategoryWithDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientUseCaseTest {
    @InjectMocks
    ClientUseCase clientUseCase;

    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    IDishPersistencePort dishPersistencePort;

    @Test
    void listRestaurant() {
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();

        when(restaurantPersistencePort.listByPageAndElements(0, 2))
                .thenReturn(Collections.singletonList(restaurantModel));

        Assertions.assertInstanceOf(List.class, clientUseCase.listRestaurant(0, 2));
    }

    @Test
    void getDishesCategorizedByRestaurant() {
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();
        CategoryDishModel categoryModel = DomainData.getCategoryModel();
        DishModel dishModel = DomainData.obtainDish(categoryModel, restaurantModel);

        when(restaurantPersistencePort.getRestaurant(restaurantModel.getId())).thenReturn(restaurantModel);
        when(dishPersistencePort.listDishesByRestaurant(String.valueOf(restaurantModel.getId()), 0, 2))
                .thenReturn(Collections.singletonList(dishModel));

        Assertions.assertInstanceOf(List.class, clientUseCase.getDishesCategorizedByRestaurant
                (String.valueOf(restaurantModel.getId()), 0, 2));
    }
}
