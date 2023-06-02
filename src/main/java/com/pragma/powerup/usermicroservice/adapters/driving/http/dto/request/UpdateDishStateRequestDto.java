package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpdateDishStateRequestDto {
    @NotNull(message = "Dish id is required")
    private Long dishId;
    @NotNull(message = "new state is required")
    private boolean active;
}
