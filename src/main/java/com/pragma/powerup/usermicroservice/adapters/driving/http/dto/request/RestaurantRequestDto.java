package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RestaurantRequestDto {
    @NotBlank(message = "Field 'name' it's required")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9 ]+$", message = "Field 'name' allows numbers but the name of the restaurant cannot be just numbers")
    private String name;
    @NotBlank(message = "Field 'address' it's required")
    private String address;
    @NotBlank(message = "Field 'idOwner' it's required")
    private String idOwner;

    @Pattern(regexp = "^\\+?57\\s(3[0-2]|7[0-1])\\d{8}$", message = "Field 'numberPhone' must be a valid number phone. Enter the format +57 313..")
    @NotBlank(message = "Field 'phoneNumber' it's required")
    private String phone;
    @NotBlank(message = "Field 'urlLogo' it's required")
    private String urlLogo;
    @Pattern(regexp = "^[0-9]+$", message = "Field 'NIT' only numbers are allowed")
    @NotBlank(message = "Field 'NIT' it's required")
    private String nit;
}
