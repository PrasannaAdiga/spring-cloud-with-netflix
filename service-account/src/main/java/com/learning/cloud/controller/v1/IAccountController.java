package com.learning.cloud.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.cloud.custom.annotation.ValidUUID;
import com.learning.cloud.model.AccountDTO;
import com.learning.cloud.exception.response.RestApiResponseErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

@RequestMapping("/v1/accounts")
@Validated
public interface IAccountController {
    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get an account by account number", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "BasicAuth"))
    ResponseEntity<AccountDTO> findByAccountNumber(@Parameter(description = "Provide a valid account number")
                                                   @PathVariable("accountNumber")
                                                   @NotBlank(message = "Account number is mandatory")
                                                   @Positive(message = "Account number should be positive")
                                                   @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
                                                           String accountNumber);

    @GetMapping(params = "ids")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get list of accounts by account ids", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "BasicAuth"))
    ResponseEntity<List<AccountDTO>> findByIds(@Parameter(description = "Provide a valid list of account ids")
                                               @RequestParam
                                               @NotEmpty(message = "Provide at-least single Id to search")
                                                       List<UUID> ids);

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new account", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "302", description = "Account already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "BasicAuth"))
    ResponseEntity<Object> create(@Valid @RequestBody AccountDTO accountDTO);

    @PutMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update an existing account", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "BasicAuth"))
    ResponseEntity<AccountDTO> update(@Valid @RequestBody AccountDTO accountDTO);

    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an existing account", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "BasicAuth"))
    ResponseEntity<Void> delete(@Parameter(description = "Provide a valid account ID to delete")
                                @PathVariable("accountId")
                                @ValidUUID
                                        UUID accountId);

    @PutMapping("/{accountNumber}/withdraw/{amount}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Withdraw given amount from given account", responses = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "BasicAuth"))
    ResponseEntity<AccountDTO> withdraw(@Parameter(description = "Provide a valid account number")
                                        @PathVariable("accountNumber")
                                        @NotBlank(message = "Account number is mandatory")
                                        @Positive(message = "Account number should be positive")
                                        @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
                                                String accountNumber,
                                        @Parameter(description = "Provide a valid amount which needs to withdraw")
                                        @PathVariable("amount")
                                        @NotNull(message = "Amount must not be null")
                                        @Positive(message = "Withdraw amount must be greater than zero")
                                                int amount) throws JsonProcessingException;

}
