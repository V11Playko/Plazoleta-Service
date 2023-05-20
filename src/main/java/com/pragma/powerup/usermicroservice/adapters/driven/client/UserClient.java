package com.pragma.powerup.usermicroservice.adapters.driven.client;


import com.pragma.powerup.usermicroservice.adapters.driven.client.adapter.FeignClientInterceptor;
import com.pragma.powerup.usermicroservice.adapters.driven.client.feignModels.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8091/users/v1/admin", configuration = FeignClientInterceptor.class)
public interface UserClient {
    @GetMapping(value = "/owner/{id}")
    User getOwner(@PathVariable("id") String id);
}