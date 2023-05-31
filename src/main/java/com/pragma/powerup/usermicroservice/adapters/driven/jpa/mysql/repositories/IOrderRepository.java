package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(
            value =
                    "SELECT COUNT(*) FROM orders "
                            + "WHERE (state = 'PENDING' OR state = 'PREPARATION' OR state = 'READY') "
                            + "AND client_id = :idClient "
                            + "AND state IS NOT NULL",
    nativeQuery = true)
    Integer getNumberOfOrdersWithStateInPreparationPendingOrReady(@Param("idClient") Long idClient);
}
