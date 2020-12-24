package com.learning.cloud.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.cloud.entity.Account;
import com.learning.cloud.exception.message.RestApiErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@RequestMapping("/v1/accounts")
@Validated
public interface IAccountController {
    @GetMapping("/{accountNumber}")
    @Operation(summary = "Get an account by account number", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Account> findByAccountNumber(@Parameter(description = "Provide a valid account number")
                                                @PathVariable("accountNumber")
                                                @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
                                                @Positive(message = "Account number should be positive value")
                                                        String accountNumber);

    @GetMapping(params = "id")
    @Operation(summary = "Get list of accounts by it's numbers", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Account.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<List<Account>> findByIds(@Parameter(description = "Provide a valid list of account ids")
                                            @RequestParam
                                            @NotEmpty(message = "Should contain at-least single Id")
                                                    List<String> ids);

    @PostMapping
    @Operation(summary = "Create a new account", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "302", description = "Account already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Object> create(@Valid @RequestBody Account account);

    @PutMapping
    @Operation(summary = "Update an existing account", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Account> update(@Valid @RequestBody Account account);

    @DeleteMapping("/{accountNumber}")
    @Operation(summary = "Delete an existing account", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Void> delete(@Parameter(description = "Provide a valid account number to delete")
                                @PathVariable("accountNumber")
                                @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
                                @Positive(message = "Account number should be positive value")
                                        String accountNumber);

    @PutMapping("/{id}/withdraw/{amount}")
    @Operation(summary = "Withdraw given amount from given account", responses = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Account> withdraw(@Parameter(description = "Provide a valid account number")
                                     @PathVariable("id")
                                     @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
                                     @Positive(message = "Account number should be positive value")
                                             String accountNumber,
                                     @Parameter(description = "Provide a valid amount which needs to withdraw")
                                     @PathVariable("amount")
                                     @NotNull(message = "Amount must not be null")
                                     @Positive(message = "Withdraw amount must be greater than zero")
                                             int amount) throws JsonProcessingException;

}
