package com.pragma.powerup.usermicroservice.adapters.driven.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Trace {
    private String id;
    private String orderId;
    private String clientId;
    private String clientEmail;
    private String date;
    private String previousState;
    private String newState;
    private String employeeId;
    private String employeeEmail;

    public Trace() {

    }
}
