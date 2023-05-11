package com.pragma.powerup.usermicroservice.adapters.driven.client.feignModels;

import lombok.Data;

import java.time.LocalDate;
@Data
public class User {
    private Long id;
    private String name;
    private String surname;
    private String dniNumber;
    private String phone;
    private LocalDate birthdate;
    private String mail;
    private String password;
    private String idRole;
}
