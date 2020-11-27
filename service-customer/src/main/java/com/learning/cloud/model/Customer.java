package com.learning.cloud.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer {
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private CustomerType customerType;

    private List<Account> accounts = new ArrayList<>();
}
