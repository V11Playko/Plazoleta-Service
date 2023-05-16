package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {

    private Long idOrder;
    private String idClient;
    private String date;
    private String state;
    private String idChef;
    private String restaurant;
}
