package com.learning.cloud.entity;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class Account {
    private Long id;

    @NonNull
    private String number;

    @NonNull
    private int balance;
}
