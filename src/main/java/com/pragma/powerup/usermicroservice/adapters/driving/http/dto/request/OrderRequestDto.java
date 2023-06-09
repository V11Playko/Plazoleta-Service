package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderRequestDto {
    @NotNull
    @Positive
    private String idRestaurant;
    @NotNull
    @Valid
    private List<OrderDishRequestDto> dishes;

}
