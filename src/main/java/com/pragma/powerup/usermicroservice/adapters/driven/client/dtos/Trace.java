package com.pragma.powerup.usermicroservice.adapters.driven.client.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Trace implements Serializable {
    private Long id;
    private Long orderId;
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
