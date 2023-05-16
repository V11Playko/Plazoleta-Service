package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DishResponseDto {

    private Long idDish;
    private String name;
    private String category;
    private String description;
    private Long price;
    private String restaurant;
    private String urlImage;
    private Boolean state;
}
