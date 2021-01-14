package com.learning.cloud.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer {
    private Long id;

    @NonNull
    @NotBlank(message = "Customer name is mandatory")
    @Size(min = 1, max = 10, message = "Customer name must be 1 to 10 digits only")
    private String name;

    @NonNull
    @NotBlank(message = "Customer type is mandatory")
    private CustomerType customerType;

    private List<Account> accounts = new ArrayList<>();
}
