package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IDishRepository;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishJpaAdapterTest {
    @InjectMocks
    DishJpaAdapter dishJpaAdapter;
    @Mock
    IDishRepository dishRepository;
    @Mock
    IDishEntityMapper dishEntityMapper;

    @Test
    void saveDish() {
        DishModel dishModel = MySqlData.obtainDish();
        DishEntity dishEntity = MySqlData.obtainDishEntity();

        when(dishEntityMapper.toEntityDish(dishModel)).thenReturn(dishEntity);
        dishJpaAdapter.saveDish(dishModel);

        verify(dishRepository).save(dishEntity);
    }

//    @Test
//    void getDish() {
//        dishJpaAdapter.getDish(anyLong());
//        verify(dishEntityMapper).toDishModel(dishRepository.findById(anyLong()).get());
//    }

    @Test
    void updateDish() {
        DishModel dish = MySqlData.obtainDish();

        dishJpaAdapter.updateDish(dish);
        verify(dishRepository).save(dishEntityMapper.toEntityDish(dish));
    }

    @Test
    void updateDishState() {
        DishModel dishModel = MySqlData.obtainDish();

        dishJpaAdapter.updateSate(dishModel);
        verify(dishRepository).save(dishEntityMapper.toEntityDish(dishModel));
    }
}