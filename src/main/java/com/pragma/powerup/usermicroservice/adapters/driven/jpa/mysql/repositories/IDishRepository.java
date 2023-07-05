package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories;


import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    Page<DishEntity> findByRestaurantIdAndActive(String restaurantId, boolean active, Pageable pageable);

    // Decidi utilizar subconsultar porque me estaba dando errores de alias
    @Query(value = "SELECT d.* FROM dish d " +
            "WHERE d.price >= :minPrice AND d.price <= :maxPrice " +
            "AND d.category_id IN (SELECT c.category_id FROM category c WHERE c.name = :preference)",
            nativeQuery = true)
    List<DishEntity> searchDishesByPriceAndPreference(@Param("minPrice") double minPrice,
                                                      @Param("maxPrice") double maxPrice,
                                                      @Param("preference") String preference);

    @Query(value = "SELECT * FROM dish d WHERE d.price >= :minPrice AND d.price <= :maxPrice", nativeQuery = true)
    List<DishEntity> searchDishesByPriceRange(@Param("minPrice") double minPrice,
                                              @Param("maxPrice") double maxPrice);

    @Query("SELECT d FROM DishEntity d WHERE d.restaurant.id = :restaurantId")
    List<DishEntity> getDishesByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query(value = "SELECT c.name, AVG(d.price) AS averagePrice " +
            "FROM category c " +
            "JOIN dish d ON d.category_id = c.category_id " +
            "GROUP BY c.name",
            nativeQuery = true)
    List<Object[]> calculateAverageByCategoryNative();
}
