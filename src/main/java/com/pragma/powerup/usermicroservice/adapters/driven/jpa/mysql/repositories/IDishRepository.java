package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories;


import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDishRepository extends JpaRepository<DishEntity, Long> {
}
