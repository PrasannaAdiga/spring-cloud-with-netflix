package com.learning.cloud.model;

import com.learning.cloud.custom.annotation.ValidUUID;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AccountDTO {
    private UUID id;

    @NotBlank(message = "Account number is mandatory")
    @Positive(message = "Account number should be positive value")
    @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
    private String number;

    @NotNull(message = "Account balance is mandatory")
    @Positive(message = "Account balance must be positive")
    private BigDecimal balance;

    @ValidUUID
    private UUID customerId;

}
