package com.pragma.powerup.usermicroservice.adapters.driving.http.controller;

import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.CancelOrderByWaitingTime;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.CreateEmployeeRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.DishUpdateRequest;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.request.UpdateDishStateRequestDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.CategoryAveragePriceResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.DishResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.dto.response.RestaurantEmployeeResponseDto;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IDishHandler;
import com.pragma.powerup.usermicroservice.adapters.driving.http.handlers.IOrderHandler;
import com.pragma.powerup.usermicroservice.configuration.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/food-court/v1/owner")
@RequiredArgsConstructor
public class OwnerRestController {
    private final IDishHandler dishHandler;
    private final IOrderHandler orderHandler;

    @Operation(summary = "Add a new Dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Dish already exists", content = @Content)
    })
    @PostMapping("/dish")
    public ResponseEntity<Void> saveDish(@Valid @RequestBody DishRequestDto dishRequestDto,@RequestParam("idOwner") String idOwner) {
        dishHandler.saveDish(dishRequestDto, idOwner);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Get dish",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The dish returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DishResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "No data found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @GetMapping("/dish/{id}")
    public ResponseEntity<DishResponseDto> getDish(@PathVariable Long id) {
        return ResponseEntity.ok(dishHandler.getDish(id));
    }

    @Operation(summary = "Update dish",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The dish update",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DishResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "No data found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PutMapping("/putDish/{id}")
    public ResponseEntity<Void> updateDish(@PathVariable("id") Long id, @RequestBody DishUpdateRequest dishUpdateRequest,@RequestParam("idOwner") String idOwner) {
        dishUpdateRequest.setId(id);
        dishHandler.updateDish(dishUpdateRequest, idOwner);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Update dish",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The dish update",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DishResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "No data found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @PutMapping("/putDishState/{id}")
    public ResponseEntity<DishResponseDto> updateDishState(@PathVariable("id") Long id, @RequestBody UpdateDishStateRequestDto dishUpdateRequest, @RequestParam("idOwner") String idOwner) {
        dishUpdateRequest.setDishId(id);
        dishHandler.updateState(dishUpdateRequest, idOwner);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(summary = "Creates a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Employee already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Owner restaurant couldn't be found", content = @Content)
    }
    )
    @PostMapping("/employee")
    public ResponseEntity<RestaurantEmployeeResponseDto> createEmployee(
            @RequestBody @Valid CreateEmployeeRequestDto createEmployeeRequestDto,
            @RequestParam("idRestaurant") String idRestaurant,
            @RequestParam("emailEmployee") String emailEmployee) {
        return new ResponseEntity<>(this.dishHandler.createEmployee(createEmployeeRequestDto, idRestaurant, emailEmployee), HttpStatus.CREATED);
    }

    @Operation(summary = "Get category dishes average",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The dish returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DishResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "No data found",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/Error")))})
    @GetMapping("/dishes-average")
    public ResponseEntity<List<CategoryAveragePriceResponseDto>> getDishesPerCategory() {
        return ResponseEntity.ok(dishHandler.calculateAverageByCategory());
    }

    @Operation(summary = "Change order to canceled state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed and notified that the order was canceled", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PostMapping("/cancel-orders")
    public ResponseEntity<Map<String,String>> cancelOrderByWaitingTime(@RequestBody CancelOrderByWaitingTime cancelOrder) {
        int tiempoLimite = cancelOrder.getTime();
        orderHandler.cancelOrderByWaitingTime(tiempoLimite);
        return ResponseEntity.status(HttpStatus.OK).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,
                        Constants.ORDERS_CANCELED_TIME + tiempoLimite + " minutes.")
        );
    }
}
