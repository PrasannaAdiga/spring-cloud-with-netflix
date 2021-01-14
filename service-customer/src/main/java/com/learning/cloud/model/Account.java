package com.learning.cloud.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Account {
    private Long id;

    @NonNull
    private String number;

    @NonNull
    private int balance;
}
