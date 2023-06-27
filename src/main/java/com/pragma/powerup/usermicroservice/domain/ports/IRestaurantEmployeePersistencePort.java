package com.pragma.powerup.usermicroservice.domain.ports;

import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;

import java.util.List;
import java.util.Optional;

public interface IRestaurantEmployeePersistencePort {
    RestaurantEmployeeModel saveEmployee(RestaurantEmployeeModel restaurantEmployeeModel);
    Optional<RestaurantEmployeeModel> findByEmployeeEmail(String email);
    List<RestaurantEmployeeModel> getEmployeesByRestaurantId(Long id);
    void deleteEmployeeByEmailAndRestaurantId(String userEmail, Long id);
}
