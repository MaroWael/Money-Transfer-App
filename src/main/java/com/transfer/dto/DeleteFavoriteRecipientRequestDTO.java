package com.transfer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteFavoriteRecipientRequestDTO {
    @NotNull
    private String recipientAccountNumber;
}
