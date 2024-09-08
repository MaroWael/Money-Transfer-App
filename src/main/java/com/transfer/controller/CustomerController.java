package com.transfer.controller;


import com.transfer.dto.ChangePasswordDTO;
import com.transfer.dto.CustomerDTO;
import com.transfer.dto.UpdateCustomerDTO;
import com.transfer.exception.custom.InvalidOldPasswordException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.response.ErrorDetails;
import com.transfer.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Controller", description = "Customer controller")
public class CustomerController {

    private final ICustomerService customerService;

    @Operation(summary = "Get customer by id")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/{customerId}")
    public CustomerDTO getCustomerById(@PathVariable Long customerId) throws ResourceNotFoundException {
        return this.customerService.getCustomerById(customerId);
    }

    @Operation(summary = "Update customer profile")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("update/{customerId}")
    public CustomerDTO updateCustomerProfile(
            @PathVariable Long customerId,
            @RequestBody UpdateCustomerDTO updateCustomerDTO) throws ResourceNotFoundException {
        return this.customerService.updateCustomerProfile(customerId, updateCustomerDTO);
    }
    @Operation(summary = "Change customer password")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Void.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/{customerId}/change-password")
    public void changePassword(
            @PathVariable Long customerId,
            @RequestBody @Valid ChangePasswordDTO changePasswordDTO)
            throws ResourceNotFoundException, InvalidOldPasswordException {
        customerService.changeCustomerPassword(customerId, changePasswordDTO);
    }
}
