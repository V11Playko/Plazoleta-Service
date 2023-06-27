package com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.entity.RestaurantEmployeeEntity;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEmployeeEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantEmployeeRepository;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantEmployeeModel;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantEmployeePersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RestaurantEmployeeJpaAdapter implements IRestaurantEmployeePersistencePort {
    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;

    @Override
    public RestaurantEmployeeModel saveEmployee(RestaurantEmployeeModel restaurantEmployeeModel) {
        RestaurantEmployeeEntity restaurantEmployeeEntity = restaurantEmployeeEntityMapper
                .toEntity(restaurantEmployeeModel);
        return restaurantEmployeeEntityMapper.toModel
                (restaurantEmployeeRepository.save(restaurantEmployeeEntity));
    }

    @Override
    public Optional<RestaurantEmployeeModel> findByEmployeeEmail(String email) {
        RestaurantEmployeeEntity restaurantEmployeeEntity = restaurantEmployeeRepository
                .findById(email).orElse(null);
        return Optional.ofNullable(restaurantEmployeeEntityMapper.toModel(restaurantEmployeeEntity));
    }

    @Override
    public List<RestaurantEmployeeModel> getEmployeesByRestaurantId(Long id) {
        List<RestaurantEmployeeEntity> employeeEntities = restaurantEmployeeRepository.findByRestaurantId(id);
        return employeeEntities.stream()
                .map(restaurantEmployeeEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEmployeeByEmailAndRestaurantId(String mail, Long id) {
        List<RestaurantEmployeeEntity> employees = restaurantEmployeeRepository.findByRestaurantId(id);
        for (RestaurantEmployeeEntity employee : employees) {
            if (employee.getUserEmail().equals(mail)) {
                restaurantEmployeeRepository.delete(employee);
            }
        }
    }
}
