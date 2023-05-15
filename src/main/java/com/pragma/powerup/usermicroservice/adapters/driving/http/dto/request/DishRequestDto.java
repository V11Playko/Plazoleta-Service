package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DishRequestDto {
    private Long idDish;
    @NotBlank(message = "Field 'name' it's required")
    private String name;
    @NotBlank(message = "Field 'category' it's required")
    private String category;
    @NotBlank(message = "Field 'description' it's required")
    private String description;
    @Positive(message = "The 'price' must be positive")
    @Digits(integer = 10, fraction = 0)
    @Min(value = 1000, message = "The price has to be in Colombian pesos.")
    @NotNull(message = "Field 'price' it's required")
    private Long price;
    @NotBlank(message = "Field 'restaurant' it's required")
    private String restaurant;
    @NotBlank(message = "Field 'urlImage' it's required")
    private String urlImage;
    @NotNull(message = "Field 'state' it's required")
    private Boolean state;
}
