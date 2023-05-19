package com.pragma.powerup.usermicroservice.adapters.driven.client.exceptions;

import com.pragma.powerup.usermicroservice.adapters.driven.jpa.mysql.exceptions.NoDataFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 401:
                return new AuthenticationException();
            case 404:
                return new NoDataFoundException();
            case 409:
                return new DataExistException();
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}
