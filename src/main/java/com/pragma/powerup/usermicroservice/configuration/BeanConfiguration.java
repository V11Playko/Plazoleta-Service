package com.pragma.powerup.usermicroservice.configuration;

import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter.DishJpaAdapter;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter.OrderJpaAdapter;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter.RestaurantJpaAdapter;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IOrderDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IOrderEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IDishRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IOrderDishRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IOrderRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.pragma.powerup.usermicroservice.domain.api.IClientServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IOwnerServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IAdminServicePort;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import com.pragma.powerup.usermicroservice.domain.usecase.ClientUseCase;
import com.pragma.powerup.usermicroservice.domain.usecase.OrderUseCase;
import com.pragma.powerup.usermicroservice.domain.usecase.OwnerUseCase;
import com.pragma.powerup.usermicroservice.domain.usecase.AdminUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    // Restaurant
    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(IRestaurantRepository restaurantRepository, IRestaurantEntityMapper restaurantEntityMapper) {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IAdminServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort, UserClient userClient){
        return new AdminUseCase(restaurantPersistencePort, userClient);
    }

    // Order
    @Bean
    public IOrderPersistencePort orderPersistencePort(IOrderRepository orderRepository, IOrderEntityMapper orderEntityMapper,
                                                      IOrderDishRepository orderDishRepository, IOrderDishEntityMapper orderDishEntityMapper){
        return new OrderJpaAdapter(orderRepository, orderEntityMapper, orderDishRepository, orderDishEntityMapper);
    }
    @Bean
    public IOrderServicePort orderServicePort(IOrderPersistencePort orderPersistencePort) {
        return new OrderUseCase(orderPersistencePort);
    }
    // Dish
    @Bean
    public IDishPersistencePort dishPersistencePort(IDishRepository dishRepository, IDishEntityMapper dishEntityMapper) {
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }
    @Bean
    public IOwnerServicePort dishServicePort(IDishPersistencePort dishPersistencePort, IAdminServicePort restaurantServicePort) {
        return new OwnerUseCase(dishPersistencePort, restaurantServicePort);
    }

    @Bean
    public IClientServicePort clientServicePort(IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IOrderPersistencePort orderPersistencePort) {
        return new ClientUseCase(restaurantPersistencePort, dishPersistencePort, orderPersistencePort);
    }
}
