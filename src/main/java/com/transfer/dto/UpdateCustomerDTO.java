package com.transfer.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateCustomerDTO {

    private String name;
    private String email;
    private String country;
    private LocalDate dateOfBirth;
}
