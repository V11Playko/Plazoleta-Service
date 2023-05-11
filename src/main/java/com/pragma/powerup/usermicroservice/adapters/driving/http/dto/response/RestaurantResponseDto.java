package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RestaurantResponseDto{
    private Long id;
    private String name;
    private String address;
    private String idOwner;
    private String phone;
    private String urlLogo;
    private String nit;
}
