package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {
    @InjectMocks
    DishUseCase dishUseCase;
    @Mock
    IDishPersistencePort dishPersistencePort;
    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    IRestaurantServicePort restaurantServicePort;

    @Test
    void saveDish() {
        RestaurantModel restaurant = DomainData.obtainRestaurant();
        CategoryDishModel categoryModel = DomainData.getCategoryModel();
        DishModel dish = DomainData.obtainDish(categoryModel, restaurant);
        String idOwner = "2";
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(Long.valueOf(idOwner));

        if (restaurantModel != null) {
            String ownerId = restaurantModel.getIdOwner();
            // Verificar si el ownerId coincide con idOwner y tomar la acción necesaria
            if (!ownerId.equals(idOwner)) {
                assertThrows(UserNotIsOwner.class, () ->{
                   throw new UserNotIsOwner();
                });
            }
            dishUseCase.saveDish(dish, idOwner);
            verify(dishPersistencePort).saveDish(dish);
        }
    }

    @Test
    void getDish() {
        dishUseCase.getDish(anyLong());

        verify(dishPersistencePort).getDish(anyLong());
    }

    @Test
    void updateDish() {
        RestaurantModel restaurant = DomainData.obtainRestaurant();
        CategoryDishModel categoryModel = DomainData.getCategoryModel();
        DishModel dish = DomainData.obtainDish(categoryModel, restaurant);
        String idOwner = "2";
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(Long.valueOf(idOwner));


        if (restaurantModel != null) {
            String ownerId = restaurantModel.getIdOwner();
            // Verificar si el ownerId coincide con idOwner y tomar la acción necesaria
            if (!ownerId.equals(idOwner)) {
                assertThrows(UserNotIsOwner.class, () ->{
                    throw new UserNotIsOwner();
                });
            }
            dishUseCase.updateDish(dish, idOwner);
            verify(dishPersistencePort).updateDish(dish);
        }
    }

    @Test
    void updateDishState() {
        RestaurantModel restaurant = DomainData.obtainRestaurant();
        CategoryDishModel categoryModel = DomainData.getCategoryModel();
        DishModel dish = DomainData.obtainDish(categoryModel, restaurant);
        String idOwner = "2";
        RestaurantModel restaurantModel = restaurantServicePort.getRestaurant(Long.valueOf(idOwner));


        if (restaurantModel != null) {
            String ownerId = restaurantModel.getIdOwner();
            // Verificar si el ownerId coincide con idOwner y tomar la acción necesaria
            if (!ownerId.equals(idOwner)) {
                assertThrows(UserNotIsOwner.class, () ->{
                    throw new UserNotIsOwner();
                });
            }
            dishUseCase.updateDishState(dish, idOwner);
            verify(dishPersistencePort).updateSate(dish);
        }
    }
    @Test
    void getDishesCategorizedByRestaurant() {
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();
        CategoryDishModel categoryModel = DomainData.getCategoryModel();
        DishModel dishModel = DomainData.obtainDish(categoryModel, restaurantModel);

        when(restaurantPersistencePort.getRestaurant(restaurantModel.getId())).thenReturn(restaurantModel);
        when(dishPersistencePort.listDishesByRestaurant(String.valueOf(restaurantModel.getId()), 0, 2))
                .thenReturn(Collections.singletonList(dishModel));

        Assertions.assertInstanceOf(List.class, dishUseCase.getDishesCategorizedByRestaurant
                (String.valueOf(restaurantModel.getId()), 0, 2));
    }
}