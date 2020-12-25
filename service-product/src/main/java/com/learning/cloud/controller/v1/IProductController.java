package com.learning.cloud.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.cloud.entity.Product;
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
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@RequestMapping("/v1/products")
@Validated
public interface IProductController {
    @GetMapping("/{id}")
    @Operation(summary = "Get a product by product id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Product> findById(@Parameter(description = "Provide a valid product id")
                                     @PathVariable("id")
                                     @Size(min = 1, max = 10, message = "Product ID must be 1 to 10 digits only")
                                     @Positive(message = "Product ID should be positive value")
                                             Long id);

    @GetMapping(params = "id")
    @Operation(summary = "Get list of products by it's ids", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<List<Product>> findByIds(@Parameter(description = "Provide a valid list of product ids")
                                            @RequestParam
                                            @NotEmpty(message = "Should contain at-least single Id")
                                                    List<Long> ids) throws JsonProcessingException;

    @PostMapping
    @Operation(summary = "Create a new product", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "302", description = "Product already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Void> create(@Valid @RequestBody Product product);

    @PutMapping
    @Operation(summary = "Update an existing product", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Product> update(@Valid @RequestBody Product product);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing product", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestApiErrorMessage.class)))})
    ResponseEntity<Void> delete(@Parameter(description = "Provide a valid product id to delete")
                                @PathVariable("id")
                                @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
                                @Positive(message = "Account number should be positive value")
                                        Long id);
}
