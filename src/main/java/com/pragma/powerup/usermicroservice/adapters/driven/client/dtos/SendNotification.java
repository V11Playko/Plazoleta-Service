package com.pragma.powerup.usermicroservice.adapters.driven.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SendNotification {
    private String phone;
    private String message;
}
