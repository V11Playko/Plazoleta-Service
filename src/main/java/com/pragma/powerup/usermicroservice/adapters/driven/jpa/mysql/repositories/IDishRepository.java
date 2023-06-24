package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories;


import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    Page<DishEntity> findByRestaurantIdAndActive(String restaurantId, boolean active, Pageable pageable);
    List<DishEntity> findDishesByCategory(String category);
}
