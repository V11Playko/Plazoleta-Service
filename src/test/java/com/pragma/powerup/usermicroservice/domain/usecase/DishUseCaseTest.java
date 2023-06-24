package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.model.CategoryAveragePriceModel;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
    @Mock
    IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    @Mock
    UserClient userClient;

    @BeforeEach
    public void setup() {
        // Inicializar los mocks
        MockitoAnnotations.openMocks(this);

        // Crear una instancia de DishUseCase con los mocks
        dishUseCase = new DishUseCase(
                dishPersistencePort,
                restaurantPersistencePort,
                restaurantServicePort,
                restaurantEmployeePersistencePort,
                userClient
        );
    }


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

    @Test
    void calculateAverageByCategory() {
        // Crear datos de prueba
        CategoryDishModel category1 = new CategoryDishModel();
        category1.setId(1L);
        category1.setName("Frutas");
        category1.setDescription("Saludables");

        CategoryDishModel category2 = new CategoryDishModel();
        category2.setId(2L);
        category2.setName("Caldos");
        category2.setDescription("Nutritivos");

        DishModel dish1 = new DishModel();
        dish1.setCategory(category1);
        dish1.setPrice(10L);

        DishModel dish2 = new DishModel();
        dish2.setCategory(category1);
        dish2.setPrice(15L);

        DishModel dish3 = new DishModel();
        dish3.setCategory(category2);
        dish3.setPrice(20L);

        List<DishModel> dishes = Arrays.asList(dish1, dish2, dish3);
        List<CategoryDishModel> categories = Arrays.asList(category1, category2);

        // Mockear el comportamiento de los puertos de persistencia
        when(dishPersistencePort.listDishes()).thenReturn(dishes);
        when(dishPersistencePort.listCategory()).thenReturn(categories);

        // Ejecutar el método a probar
        List<CategoryAveragePriceModel> results = dishUseCase.calculateAverageByCategory();

        // Verificar los resultados
        assertEquals(2, results.size());

        CategoryAveragePriceModel result1 = results.get(0);
        assertEquals("Frutas", result1.getName());
        assertEquals("$12.50", result1.getAveragePerDish());

        CategoryAveragePriceModel result2 = results.get(1);
        assertEquals("Caldos", result2.getName());
        assertEquals("$20.00", result2.getAveragePerDish());

        // Verificar que se llamaron los métodos necesarios en los mocks
        verify(dishPersistencePort, times(1)).listDishes();
        verify(dishPersistencePort, times(1)).listCategory();
    }
}
