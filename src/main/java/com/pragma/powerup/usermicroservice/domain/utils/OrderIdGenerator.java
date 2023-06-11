package com.pragma.powerup.usermicroservice.domain.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class OrderIdGenerator {
    private static final String CONFIG_FILE_PATH = "config.json";
    private static Long orderIdCounter;

    static {
        loadConfig();
    }

    public static synchronized Long generateOrderId() {
        Long orderId = orderIdCounter;
        orderIdCounter++;
        saveConfig();
        return orderId;
    }

    private static void loadConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Config config = objectMapper.readValue(new File(CONFIG_FILE_PATH), Config.class);
            orderIdCounter = config.orderIdCounter;
        } catch (IOException e) {
            // Error al cargar la configuración, se usa el valor predeterminado
            orderIdCounter = 1L;
        }
    }

    private static void saveConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        Config config = new Config(orderIdCounter);
        try {
            objectMapper.writeValue(new File(CONFIG_FILE_PATH), config);
        } catch (IOException e) {
            // Error al guardar la configuración
            e.printStackTrace();
        }
    }

    private static class Config {
        @JsonProperty("orderIdCounter")
        private Long orderIdCounter;

        public Config(Long orderIdCounter) {
            this.orderIdCounter = orderIdCounter;
        }

        public Long getOrderIdCounter() {
            return orderIdCounter;
        }

        public void setOrderIdCounter(Long orderIdCounter) {
            this.orderIdCounter = orderIdCounter;
        }
    }
}
