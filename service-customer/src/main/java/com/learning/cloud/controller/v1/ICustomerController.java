package com.learning.cloud.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.cloud.entity.Customer;
import com.learning.cloud.exception.response.RestApiResponseErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@RequestMapping("/v1/customers")
@Validated
public interface ICustomerController {
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get a customer by customer id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))})
    ResponseEntity<Customer> findById(@Parameter(description = "Provide a valid customer id")
                                      @PathVariable
                                      @Positive(message = "Customer ID should be positive value")
                                              Long id);

    @GetMapping(params = "id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get list of customers by it's numbers", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))})
    ResponseEntity<List<Customer>> findByIds(@Parameter(description = "Provide a valid list of customer ids")
                                             @RequestParam
                                             @NotEmpty(message = "Should contain at-least single Id")
                                                     List<Long> ids);

    @GetMapping("/{id}/withAccounts")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get a customer by customer id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))})
    ResponseEntity<Customer> findByIdWithAccounts(@Parameter(description = "Provide a valid customer id")
                                                  @PathVariable("id")
                                                  @Positive(message = "Customer ID should be positive value")
                                                          Long customerId) throws JsonProcessingException;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new customer", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "302", description = "Account already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))})
    ResponseEntity<Customer> add(@Valid @RequestBody Customer customer);

    @PutMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update an existing customer", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))})
    ResponseEntity<Customer> update(@Valid @RequestBody Customer customer);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an existing customer", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))})
    ResponseEntity<Void> delete(@Parameter(description = "Provide a valid customer id to delete")
                                @PathVariable("id")
                                @Positive(message = "Customer ID should be positive value")
                                        Long id);

}
