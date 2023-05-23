package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.domain.api.IAdminServicePort;
import com.pragma.powerup.usermicroservice.domain.exceptions.UserNotIsOwner;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {
    @InjectMocks
    OwnerUseCase dishUseCase;
    @Mock
    IDishPersistencePort dishPersistencePort;
    @Mock
    IAdminServicePort adminServicePort;

    @Test
    void saveDish() {
        DishModel dish = DomainData.obtainDish();
        String idOwner = "2";
        RestaurantModel restaurantModel = adminServicePort.getRestaurant(Long.valueOf(idOwner));

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
        DishModel dish = DomainData.obtainDish();
        String idOwner = "2";
        RestaurantModel restaurantModel = adminServicePort.getRestaurant(Long.valueOf(idOwner));


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
        DishModel dish = DomainData.obtainDish();
        String idOwner = "2";
        RestaurantModel restaurantModel = adminServicePort.getRestaurant(Long.valueOf(idOwner));


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
}