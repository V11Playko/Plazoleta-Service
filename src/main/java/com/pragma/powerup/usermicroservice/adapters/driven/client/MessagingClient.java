package com.pragma.powerup.usermicroservice.adapters.driven.client;

import com.pragma.powerup.usermicroservice.adapters.driven.client.interceptor.FeignClientInterceptor;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.SendNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Messaging-Service",
        url = "http://localhost:8093/messaging/v1",
        configuration = FeignClientInterceptor.class)
public interface MessagingClient {

    @PostMapping(value = "/employee/send-notification")
    boolean sendMessageToClient(@RequestBody SendNotification sendNotification);
}
