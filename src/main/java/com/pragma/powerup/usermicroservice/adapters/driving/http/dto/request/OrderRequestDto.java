package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request;


import com.pragma.powerup.usermicroservice.domain.model.OrdersDishesModel;
import com.pragma.powerup.usermicroservice.domain.model.RestaurantModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderRequestDto {
    private Long idOrder;
    private String idClient;
    private String date;
    private String state;
    private String idChef;
    private String restaurant;

}
