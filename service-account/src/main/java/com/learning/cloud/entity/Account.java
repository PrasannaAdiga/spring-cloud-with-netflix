package com.learning.cloud.entity;

import com.learning.cloud.custom.annotation.ValidUUID;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Audited
@NoArgsConstructor
@Getter
@Setter
public class Account extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @ColumnDefault("random_uuid()")
    @Type(type = "uuid-char")
    @ValidUUID
    private UUID id;

    @NotBlank(message = "Account number is mandatory")
    @Size(min = 1, max = 10, message = "Account number must be 1 to 10 digits only")
    @Positive(message = "Account number should be positive value")
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull(message = "Account balance is mandatory")
    @Positive(message = "Account balance must be positive")
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @ColumnDefault("random_uuid()")
    @Type(type = "uuid-char")
    @Column(name = "customer_id", nullable = false)
    @ValidUUID
    private UUID customerId;

}
