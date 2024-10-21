package com.transfer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteRecipientDTO {

    @NotNull
    private String recipientName;
    @NotNull
    private String recipientAccountNumber;
}
