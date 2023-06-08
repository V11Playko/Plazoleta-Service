package com.pragma.powerup.usermicroservice.adapters.driven.client;


import com.pragma.powerup.usermicroservice.adapters.driven.client.interceptor.FeignClientInterceptor;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service",
        url = "http://localhost:8091/users/v1",
        configuration = FeignClientInterceptor.class)
public interface UserClient {

    @GetMapping(value = "/admin/owner/{id}")
    User getUser(@PathVariable("id") String id);

    @GetMapping(value = "/owner/{id}")
    User getOwner(@PathVariable("id") String id);
    @GetMapping(value = "/client/{id}")
    User getClient(@PathVariable("id") String id);
    @GetMapping(value = "/employee/client/{id}")
    User getClientByEmployee(@PathVariable("id") String id);
    @GetMapping(value = "/auth/getUser")
    User getUserByEmail(@RequestParam("email") String email);
}