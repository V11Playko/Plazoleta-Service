package com.pragma.powerup.usermicroservice.adapters.driven.client.adapter;

import com.pragma.powerup.usermicroservice.adapters.driven.client.MessagingClient;
import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.SendNotification;
import com.pragma.powerup.usermicroservice.domain.ports.IMessagingPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessagingAdapter implements IMessagingPersistencePort {
    private final MessagingClient messagingClient;
    @Override
    public boolean notifyClient(String message, String phoneNumber) {
        return messagingClient.sendMessageToClient(new SendNotification(phoneNumber, message));
    }
}
