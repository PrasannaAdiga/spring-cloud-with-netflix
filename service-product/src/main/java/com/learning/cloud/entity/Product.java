package com.learning.cloud.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Product {
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private int price;
}
