package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetDishResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String urlImage;
}
