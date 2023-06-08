package com.pragma.powerup.usermicroservice.domain.ports;

public interface IMessagingPersistencePort {
    boolean notifyClient(String message, String phoneNumber);
}
