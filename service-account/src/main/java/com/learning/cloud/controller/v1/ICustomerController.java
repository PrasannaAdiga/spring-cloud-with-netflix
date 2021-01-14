package com.learning.cloud.controller.v1;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@RequestMapping("/customers")
@Validated
public interface ICustomerController {
    @GetMapping("/{customerId}/accounts")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all customer accounts", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiResponseErrorMessage.class)))},
            security = @SecurityRequirement(name = "BasicAuth"))
    ResponseEntity<List<AccountDTO>> findAccountsByCustomerId(@Parameter(description = "Provide a valid customer ID")
                                                           @PathVariable("customerId")
                                                           @Size(min = 1, max = 3, message = "Customer Id must be 1 to 3 digits only")
                                                           @Positive(message = "Customer Id should be positive value")
                                                                      UUID customerId);
}
