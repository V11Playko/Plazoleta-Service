package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ListOrdersRequestDto {
    @Pattern(regexp = "^(PENDING|PREPARATION|CANCELED|READY|DELIVERED)$",
            message = "Are only allowed: PENDING, PREPARATION, CANCELED, READY or DELIVERED ")
    @NotBlank(message = "Field 'orderState' it's required")
    String orderState;
    @PositiveOrZero(message = "Page number must be positive or zero")
    @NotNull(message = "Field 'page' it's required")
    int page;
    @Positive(message = "Elements per page must be positive")
    @NotNull(message = "Field 'elementsXpage' it's required")
    int elementsXpage;
}
