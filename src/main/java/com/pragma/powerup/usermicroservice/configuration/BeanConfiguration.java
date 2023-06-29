package com.pragma.powerup.usermicroservice.configuration;

import com.pragma.powerup.usermicroservice.adapters.driven.client.MessagingClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.TraceabilityClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.UserClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.adapter.MessagingAdapter;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter.DishJpaAdapter;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter.OrderJpaAdapter;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter.RestaurantEmployeeJpaAdapter;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.adapter.RestaurantJpaAdapter;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.ICategoryEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IOrderDishEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IOrderEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEmployeeEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.ICategoryRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IDishRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IOrderDishRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IOrderRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantEmployeeRepository;
import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.pragma.powerup.usermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IDishServicePort;
import com.pragma.powerup.usermicroservice.domain.api.IRestaurantServicePort;
import com.pragma.powerup.usermicroservice.domain.ports.IDishPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IMessagingPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IOrderPersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.usermicroservice.domain.ports.IRestaurantPersistencePort;
import com.pragma.powerup.usermicroservice.domain.usecase.OrderUseCase;
import com.pragma.powerup.usermicroservice.domain.usecase.DishUseCase;
import com.pragma.powerup.usermicroservice.domain.usecase.RestaurantUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    // Restaurant
    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort(IRestaurantRepository restaurantRepository, IOrderRepository orderRepository,IRestaurantEntityMapper restaurantEntityMapper) {
        return new RestaurantJpaAdapter(restaurantRepository, orderRepository, restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort,
                                                        IDishPersistencePort dishPersistencePort,
                                                        IOrderPersistencePort orderPersistencePort,
                                                        IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                                                        UserClient userClient){
        return new RestaurantUseCase(restaurantPersistencePort, dishPersistencePort, orderPersistencePort, restaurantEmployeePersistencePort, userClient);
    }

    // Order
    @Bean
    public IOrderPersistencePort orderPersistencePort(IOrderRepository orderRepository, IOrderEntityMapper orderEntityMapper,
                                                      IOrderDishRepository orderDishRepository, IOrderDishEntityMapper orderDishEntityMapper,
                                                        TraceabilityClient traceClient){
        return new OrderJpaAdapter(orderRepository, orderEntityMapper, orderDishRepository, orderDishEntityMapper, traceClient);
    }
    @Bean
    public IOrderServicePort orderServicePort(IRestaurantPersistencePort restaurantPersistencePort,
                                              IDishPersistencePort dishPersistencePort,
                                              IOrderPersistencePort orderPersistencePort,
                                              IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                                              IMessagingPersistencePort messagingClient,
                                              UserClient userClient) {
        return new OrderUseCase(restaurantPersistencePort, dishPersistencePort, orderPersistencePort, restaurantEmployeePersistencePort, userClient, messagingClient);
    }
    // Dish
    @Bean
    public IDishPersistencePort dishPersistencePort(IDishRepository dishRepository,
                                                    ICategoryRepository categoryRepository,
                                                    IDishEntityMapper dishEntityMapper,
                                                    ICategoryEntityMapper categoryEntityMapper) {
        return new DishJpaAdapter(dishRepository, categoryRepository ,dishEntityMapper, categoryEntityMapper);
    }
    @Bean
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort,
                                            IRestaurantPersistencePort restaurantPersistencePort,
                                            IRestaurantServicePort restaurantServicePort,
                                            IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                                            UserClient userClient) {
        return new DishUseCase(dishPersistencePort, restaurantPersistencePort, restaurantServicePort, restaurantEmployeePersistencePort, userClient);
    }
    // Employee

    @Bean
    public IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort(IRestaurantEmployeeRepository restaurantEmployeeRepository,
                                                                                IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper) {
        return new RestaurantEmployeeJpaAdapter(restaurantEmployeeRepository, restaurantEmployeeEntityMapper);
    }

    @Bean
    public IMessagingPersistencePort messagingPersistencePort(MessagingClient messagingClient) {
        return new MessagingAdapter(messagingClient);
    }
}
