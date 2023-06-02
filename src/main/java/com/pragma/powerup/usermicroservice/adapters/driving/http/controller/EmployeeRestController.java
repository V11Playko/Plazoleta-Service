package com.pragma.powerup.usermicroservice.adapters.driving.http.controller;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.ListOrdersRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/food-court/v1/employee")
@RequiredArgsConstructor
public class EmployeeRestController {
    private final IOrderHandler orderHandler;

    @Operation(summary = "Get orders filtered by state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders", content = @Content),
            @ApiResponse(responseCode = "500", description = "Employee doesn't belong to any restaurant", content = @Content),
    })
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> listOrders(
            @Valid @RequestBody ListOrdersRequestDto listOrdersRequestDto,
            @RequestParam("employeeEmail") String employeeEmail
    ) {
        return ResponseEntity.ok(orderHandler.listOrdersByState(listOrdersRequestDto.getOrderState(),
                listOrdersRequestDto.getPage(), listOrdersRequestDto.getElementsXpage(), employeeEmail));
    }
}
