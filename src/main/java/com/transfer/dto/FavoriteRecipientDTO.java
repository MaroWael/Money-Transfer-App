package com.transfer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteRecipientDTO {

    private String recipientName;
    private String recipientAccountNumber;
}
