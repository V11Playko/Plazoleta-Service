package com.pragma.powerup.usermicroservice.configuration.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BearerTokenWrapper {
    private String token;
}
