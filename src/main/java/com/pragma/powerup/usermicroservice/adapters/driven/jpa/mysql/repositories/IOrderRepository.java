package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderStateType;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(
            value =
                    "SELECT COUNT(*) FROM orders "
                            + "WHERE (state = 'PENDIENTE' OR state = 'EN_PREPARACION' OR state = 'LISTO') "
                            + "AND client_id = :idClient "
                            + "AND state IS NOT NULL",
    nativeQuery = true)
    Integer getNumberOfOrdersWithStateInPreparationPendingOrReady(@Param("idClient") Long idClient);

    List<OrderEntity> findByRestaurantIdAndState(Long restaurantId, OrderStateType orderStateType, Pageable pageable);

    Optional<OrderEntity> findByRestaurantIdAndId(Long restaurantId, Long orderId);

    List<OrderEntity> findBySecurityPinAndState(String securityPin, OrderStateType orderStateType);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.state = 'PENDIENTE' AND o.restaurant.id = :restaurantId")
    Integer countPendingOrdersByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT o FROM OrderEntity o WHERE o.restaurant.id = :restaurantId")
    List<OrderEntity> getOrdersByRestaurantId(Long restaurantId);

}
