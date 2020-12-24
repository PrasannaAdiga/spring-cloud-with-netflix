package com.learning.cloud.controller.v1;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@RequestMapping("/customers")
@Validated
public interface ICustomerController {
    @GetMapping("/{customerId}/accounts")
    @Operation(summary = "Get all customer accounts", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Account.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<List<Account>> findAccountsByCustomerId(@Parameter(description = "Provide a valid customer ID")
                                                           @PathVariable("customerId")
                                                           @Size(min = 1, max = 3, message = "Customer Id must be 1 to 3 digits only")
                                                           @Positive(message = "Customer Id should be positive value")
                                                                   String customerId);
}
