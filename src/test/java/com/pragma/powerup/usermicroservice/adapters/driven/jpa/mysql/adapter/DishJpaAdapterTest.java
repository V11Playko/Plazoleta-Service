package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IDishRepository;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.impl.HttpData;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.usecase.DomainData;
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
        RestaurantModel restaurant = MySqlData.obtainRestaurant();
        CategoryDishModel categoryModel = MySqlData.getCategoryModel();
        DishModel dishModel = MySqlData.obtainDish(categoryModel, restaurant);
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
        RestaurantModel restaurant = MySqlData.obtainRestaurant();
        CategoryDishModel categoryModel = MySqlData.getCategoryModel();
        DishModel dishModel = MySqlData.obtainDish(categoryModel, restaurant);

        dishJpaAdapter.updateDish(dishModel);
        verify(dishRepository).save(dishEntityMapper.toEntityDish(dishModel));
    }

    @Test
    void updateDishState() {
        RestaurantModel restaurant = MySqlData.obtainRestaurant();
        CategoryDishModel categoryModel = MySqlData.getCategoryModel();
        DishModel dishModel = MySqlData.obtainDish(categoryModel, restaurant);

        dishJpaAdapter.updateSate(dishModel);
        verify(dishRepository).save(dishEntityMapper.toEntityDish(dishModel));
    }
}