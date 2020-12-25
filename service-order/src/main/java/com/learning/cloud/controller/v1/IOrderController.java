package com.learning.cloud.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.cloud.entity.Order;
import com.learning.cloud.exception.message.RestApiErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequestMapping("/v1/orders")
@Validated
public interface IOrderController {
    @PostMapping
    @Operation(summary = "Prepare for a new order", responses = {
            @ApiResponse(responseCode = "201", description = "Order is prepared", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Valid customer/account not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Void> prepare(@Valid @RequestBody Order order) throws JsonProcessingException;

    @PutMapping("/{id}")
    @Operation(summary = "Accept a new order", responses = {
            @ApiResponse(responseCode = "201", description = "Order is accepted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Order> accept(@Parameter(description = "Provide a valid order number")
                                 @PathVariable
                                 @Positive(message = "Order ID should be positive value")
                                         Long id) throws JsonProcessingException;

}
