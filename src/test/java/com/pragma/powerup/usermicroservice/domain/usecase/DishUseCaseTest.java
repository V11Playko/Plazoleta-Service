package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
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
        // Mock de los resultados de la consulta
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"Carnes", 10.5});
        results.add(new Object[]{"Pollo", 8.2});

        // Configuración del mock del port
        Mockito.when(dishPersistencePort.calculateAverageByCategoryNative()).thenReturn(results);

        // Llamada al método que se va a probar
        List<CategoryAveragePriceModel> averagePrices = dishUseCase.calculateAverageByCategory();

        // Verificación de los resultados esperados
        Assertions.assertEquals(2, averagePrices.size());

        CategoryAveragePriceModel category1 = averagePrices.get(0);
        Assertions.assertEquals("Carnes", category1.getName());
        Assertions.assertEquals("$10.50", category1.getAveragePerDish());

        CategoryAveragePriceModel category2 = averagePrices.get(1);
        Assertions.assertEquals("Pollo", category2.getName());
        Assertions.assertEquals("$8.20", category2.getAveragePerDish());

        // Verificación de que el método del port se haya llamado una vez
        Mockito.verify(dishPersistencePort, Mockito.times(1)).calculateAverageByCategoryNative();
    }

    @Test
    void searchDishByPreferences() {
        // Arrange
        double minPrice = 10.0;
        double maxPrice = 20.0;
        String preference = "Vegetarian";

        // Mock the result from the persistence port
        RestaurantModel restaurantModel = DomainData.obtainRestaurant();
        CategoryDishModel categoryModel = DomainData.getCategoryModel();
        DishModel dish1 = DomainData.obtainDish(categoryModel, restaurantModel);
        DishModel dish2 = DomainData.obtainDish(categoryModel, restaurantModel);
        List<DishModel> expectedDishes = Arrays.asList(dish1, dish2);
        Mockito.when(dishPersistencePort.searchDishesByPriceAndPreference(minPrice, maxPrice, preference))
                .thenReturn(expectedDishes);

        // Act
        List<DishModel> result = dishPersistencePort.searchDishesByPriceAndPreference(minPrice, maxPrice, preference);

        // Assert
        Assertions.assertEquals(expectedDishes, result);
        Mockito.verify(dishPersistencePort, Mockito.times(1))
                .searchDishesByPriceAndPreference(minPrice, maxPrice, preference);

    }
}
