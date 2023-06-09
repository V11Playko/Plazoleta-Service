package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Long idClient;
    private LocalDateTime date;
    private String state;
    private String securityPin;
    private String emailChef;
    private String restaurant;
}
