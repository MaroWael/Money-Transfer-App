package com.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {
    private String fromAccountNumber;
    private String toAccountNumber;
    private String fromAccountName;
    private String toAccountName;
    private Double amount;
    private String transactionDate; // ISO 8601 format
}
