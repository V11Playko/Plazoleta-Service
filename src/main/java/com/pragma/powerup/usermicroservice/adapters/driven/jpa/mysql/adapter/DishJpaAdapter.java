package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.ICategoryEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.ICategoryRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IDishRepository;
import com.pragma.powerup.usermicroservice.domain.model.CategoryAveragePriceModel;
import com.pragma.powerup.usermicroservice.domain.model.CategoryDishModel;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {
    private final IDishRepository dishRepository;
    private final ICategoryRepository categoryRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public DishEntity saveDish(DishModel dishModel) {
        DishEntity dishEntity = dishEntityMapper.toEntityDish(dishModel);
        return dishRepository.save(dishEntity);
    }

    @Override
    public DishModel getDish(Long id) {
        return dishEntityMapper.toDishModel(dishRepository.findById(id).get());
    }

    @Override
    public void updateDish(DishModel dishModel) {
        dishRepository.save(dishEntityMapper.toEntityDish(dishModel));
    }

    @Override
    public void updateSate(DishModel dishModel) {
        dishRepository.save(dishEntityMapper.toEntityDish(dishModel));
    }

    @Override
    public List<DishModel> listDishesByRestaurant(String idRestaurant, int page, int elementsXpage) {
        Pageable pageable = PageRequest.of(page, elementsXpage);
        Page<DishEntity> dishEntityPage = dishRepository.findByRestaurantIdAndActive(idRestaurant, true, pageable);
        List<DishEntity> dishEntityList = dishEntityPage.getContent();
        return dishEntityList.stream()
                .map(dishEntityMapper::toDishModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<DishModel> listDishes() {
        List<DishEntity> dishEntityList = dishRepository.findAll();

        return dishEntityList.stream()
                .map(dishEntityMapper::toDishModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDishModel> listCategory() {
        List<CategoryEntity> categoryDish = categoryRepository.findAll();

        return categoryDish.stream()
                .map(categoryEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<DishModel> searchDishesByPriceAndPreference(double minPrice, double maxPrice, String preference) {
        List<DishEntity> dishEntities =
                dishRepository.searchDishesByPriceAndPreference(minPrice, maxPrice, preference);

        return dishEntities.stream()
                .map(dishEntityMapper::toDishModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<DishModel> searchDishesByPriceRange(double minPrice, double maxPrice) {
        List<DishEntity> dishEntities =
                dishRepository.searchDishesByPriceRange(minPrice, maxPrice);
        return dishEntities.stream()
                .map(dishEntityMapper::toDishModel)
                .collect(Collectors.toList());
    }
    public List<DishModel> getDishesByRestaurantId(Long id) {
        List<DishEntity> dishEntities = dishRepository.getDishesByRestaurantId(id);
        return dishEntities.stream()
                .map(dishEntityMapper::toDishModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDishById(Long id) {
        dishRepository.deleteById(id);
    }

    @Override
    public List<Object[]> calculateAverageByCategoryNative() {
        return dishRepository.calculateAverageByCategoryNative();
    }
}
