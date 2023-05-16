package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DishUpdateRequest {
    private Long id;
    @NotBlank(message = "Field 'description' it's required for update")
    private String description;
    @NotNull(message = "Field 'price' it's required for update")
    private Long price;
}
