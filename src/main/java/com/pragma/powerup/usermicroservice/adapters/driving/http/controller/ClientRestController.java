package com.pragma.powerup.usermicroservice.adapters.driving.http.controller;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.OrderRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryDishesResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.ListRestaurantForClientResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IDishHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IRestaurantHandler;
import com.pragma.powerup.usermicroservice.configuration.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/food-court/v1/client")
@RequiredArgsConstructor
public class ClientRestController {
    private final IOrderHandler orderHandler;
    private final IDishHandler dishHandler;
    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Get all the restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurants list returned", content = @Content),
            @ApiResponse(responseCode = "409", description = "Restaurant already exists", content = @Content)
    })
    @GetMapping("/list-restaurants")
    public ResponseEntity<List<ListRestaurantForClientResponseDto>> listRestaurant(
            @Positive @RequestParam("page") int page,
            @Positive @RequestParam("elementsXPage") int elementsXPage
    ){
        return ResponseEntity.ok(restaurantHandler.listRestaurant(page, elementsXPage));
    }

    @Operation(summary = "Get all dishes of a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dishes list returned", content = @Content),
            @ApiResponse(responseCode = "409", description = "Dishes already exists", content = @Content)
    })
    @GetMapping("/list-dishes-restaurant")
    public ResponseEntity<List<CategoryDishesResponseDto>> getListDishesByRestaurant(
            @RequestParam("idRestaurant") String idRestaurant,
            @Positive @RequestParam("page") int page,
            @Positive @RequestParam("elementsXPage") int elementsXPage
    ) {
        return ResponseEntity.ok(dishHandler.getDishesCategorizedByRestaurant(idRestaurant, page, elementsXPage));
    }

    @PostMapping("/new-order")
    public ResponseEntity<Map<String,String>> generarPedido(@Valid @RequestBody OrderRequestDto orderRequestDto,
                                                            @RequestParam("idClient") String idClient){
        orderHandler.newOrder(orderRequestDto.getIdRestaurant(),idClient, orderRequestDto.getDishes());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.ORDER_CREATED)
        );
    }

    @Operation(summary = "Change order to canceled state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "State changed or notified that can't be cancelled", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PutMapping("/order-cancel")
    public ResponseEntity<Void> cancelOrder(
            @RequestParam("orderId") @Valid Long orderId,
            @RequestParam("clientEmail") @Valid String clientEmail) {
        orderHandler.cancelOrder(orderId, clientEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
