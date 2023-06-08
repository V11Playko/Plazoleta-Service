package com.pragma.powerup.usermicroservice.adapters.driving.http.controller;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.ListOrdersRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.AssignOrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.OrderResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @Operation(summary = "Assign multiple orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders", content = @Content),
            @ApiResponse(responseCode = "500", description = "Employee doesn't belong to any restaurant", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order doesn't exists", content = @Content),
            @ApiResponse(responseCode = "400", description = "Order is already assigned", content = @Content)
    })
    @PutMapping("/assign-orders")
    public ResponseEntity<List<AssignOrderResponseDto>> assignOrders(
            @RequestBody @Valid List<Long> orderIds,
            @RequestParam("employeeEmail") String employeeEmail
    ) {
        return ResponseEntity.ok(this.orderHandler.assignOrdersToEmployee(orderIds, employeeEmail));
    }

    @Operation(summary = "Change order to ready state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "State changed and notified", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PutMapping("/order-ready")
    public ResponseEntity<Void> changeOrderToReady(
            @RequestParam("orderId") @Valid Long orderId,
            @RequestParam("employeeEmail") String employeeEmail) {
        orderHandler.changeOrderToReady(orderId, employeeEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
