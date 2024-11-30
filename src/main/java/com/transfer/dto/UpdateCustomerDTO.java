package com.transfer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
public class UpdateCustomerDTO {

    @Pattern(regexp = "^$|^(?!\\s*$).+", message = "Name cannot be empty or whitespace only")
    @Length(min = 1, message = "Name must be at least 1 character long")
    private String name;

    @Email(message = "Email should be valid")
    @Length(min = 1, message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^$|^(?!\\s*$).+", message = "Country cannot be empty or whitespace only")
    @Length(min = 1, message = "Country must be at least 1 character long")
    private String country;

    private LocalDate dateOfBirth;
}
