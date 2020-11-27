package com.learning.cloud.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Account {
    private Long id;

    @NonNull
    private String number;

    @NonNull
    private int balance;

    @NonNull
    private Long customerId;


}
