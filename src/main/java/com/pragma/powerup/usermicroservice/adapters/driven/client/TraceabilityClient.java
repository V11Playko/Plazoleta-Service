package com.pragma.powerup.usermicroservice.adapters.driven.client;

import com.pragma.powerup.usermicroservice.adapters.driven.client.dtos.Trace;
import com.pragma.powerup.usermicroservice.adapters.driven.client.interceptor.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "traceability-service",
        url = "http://localhost:8095/trace",
        configuration = FeignClientInterceptor.class)
public interface TraceabilityClient {

    @GetMapping(value = "/{id}")
    Trace getTrace(@PathVariable("id") Long id);

    @PostMapping("/saveTrace")
    void saveTrace(@RequestBody Trace trace);

    @PutMapping("/putInvoice")
    void putTrace(@RequestBody Trace trace);
}
