package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IDishRepository;
import com.pragma.powerup.usermicroservice.domain.model.DishModel;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {
    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

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
}
