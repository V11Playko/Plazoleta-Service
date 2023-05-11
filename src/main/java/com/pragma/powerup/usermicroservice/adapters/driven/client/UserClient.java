package com.pragma.powerup.usermicroservice.adapters.driven.client;


import com.pragma.powerup.usermicroservice.adapters.driven.client.adapter.FeignClientConfig;
import com.pragma.powerup.usermicroservice.adapters.driven.client.feignModels.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service", url = "http://localhost:8091/user", configuration = FeignClientConfig.class)
public interface UserClient {
    @GetMapping(value = "/owner/{id}")
    Optional<User> getOwner(@PathVariable("id") String id);
}