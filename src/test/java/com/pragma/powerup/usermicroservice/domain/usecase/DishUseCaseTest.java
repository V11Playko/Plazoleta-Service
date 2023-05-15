package com.pragma.powerup.usermicroservice.domain.usecase;

import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {
    @InjectMocks
    DishUseCase dishUseCase;
    @Mock
    IDishPersistencePort dishPersistencePort;

    @Test
    void saveDish() {
        DishModel dish = DomainData.obtainDish();

        dishUseCase.saveDish(dish);
        verify(dishPersistencePort).saveDish(dish);
    }
}