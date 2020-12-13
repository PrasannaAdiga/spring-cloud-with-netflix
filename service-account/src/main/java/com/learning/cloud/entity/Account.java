package com.learning.cloud.entity;

import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Account {
    private String id;

    @NonNull
    @NotBlank(message = "Account number is mandatory")
    @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
    @Positive(message = "Account number should be positive value")
    private String number;

    @NonNull
    @NotNull(message = "Account balance is mandatory")
    @Positive(message = "Account balance must be positive")
    private BigDecimal balance;

    @NonNull
    @NotBlank(message = "Customer Id is mandatory for an account")
    @Size(min = 1, max = 3, message = "Customer Id must be 1 to 3 digits only")
    @Positive(message = "Customer Id must be positive")
    private String customerId;

}
