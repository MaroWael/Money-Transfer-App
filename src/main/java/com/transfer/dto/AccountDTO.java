package com.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private Long id;

    private String accountNumber;

    private Double balance;

    private String accountName;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
