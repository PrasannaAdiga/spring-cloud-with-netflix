package com.learning.cloud.entity;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Product {
    private Long id;

    @NonNull
    @NotBlank(message = "Product name is mandatory")
    @Size(min = 1, max = 10, message = "Product name must be 1 to 10 digits only")
    @Positive(message = "Account number should be positive value")
    private String name;

    @NonNull
    @NotNull(message = "Product price is mandatory")
    @Size(min = 1, max = 5, message = "Product price must be 1 to 5 digits only")
    @Positive(message = "Product price should be positive value")
    private int price;
}
