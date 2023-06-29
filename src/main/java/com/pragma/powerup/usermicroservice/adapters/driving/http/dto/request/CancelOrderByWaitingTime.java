package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CancelOrderByWaitingTime {
    @NotNull(message = "Field 'time' it's required")
    private int time;

    public CancelOrderByWaitingTime() {
        // Constructor vacío requerido para la deserialización
    }
}
