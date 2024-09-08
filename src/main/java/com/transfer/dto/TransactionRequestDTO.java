package com.transfer.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class TransactionRequestDTO {

    @NotNull
    private String toAccountNumber;

    @NotNull
    private Double amount;

    @NotNull
    private String recipientName;
}
