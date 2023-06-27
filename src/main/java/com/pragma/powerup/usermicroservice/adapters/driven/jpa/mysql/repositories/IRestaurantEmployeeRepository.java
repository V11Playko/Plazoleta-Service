package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRestaurantEmployeeRepository extends JpaRepository<RestaurantEmployeeEntity, String> {
    List<RestaurantEmployeeEntity> findByRestaurantId(Long restaurantId);

}
