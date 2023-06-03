package com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class AssignOrderResponseDto {
    private Long id;
    private Date date;
    private String clientEmail;
    private String state;
    private String chefEmail;
}
