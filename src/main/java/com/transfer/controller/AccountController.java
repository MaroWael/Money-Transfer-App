package com.transfer.controller;

import com.transfer.constants.ApplicationConstants;
import com.transfer.constants.BusinessConstants;
import com.transfer.dto.AccountDTO;
import com.transfer.dto.DepositRequestDTO;
import com.transfer.dto.MessageResponseDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.response.ErrorDetails;
import com.transfer.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Validated
@Tag(name = "Account Controller", description = "Account controller")
@CrossOrigin
public class AccountController {

    private final IAccountService accountService;

    @Operation(summary = "Get Account by Id")
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_200, content = {@Content(schema = @Schema(implementation = AccountDTO.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_404, content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @GetMapping("/{accountId}")
    public AccountDTO getAccountById(@PathVariable @Positive @NotNull(message = "Amount cannot be null") Long accountId) throws ResourceNotFoundException {
        return this.accountService.getAccountById(accountId);
    }

    @Operation(summary = "Deposit Money")
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_200, content = {@Content(mediaType = BusinessConstants.APPLICATION_JSON)})
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_400, content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @PutMapping("/{accountId}/deposit")
    public ResponseEntity<MessageResponseDTO> deposit(@PathVariable @Positive @NotNull(message = "Amount cannot be null") Long accountId, @Valid @RequestBody DepositRequestDTO depositRequest) throws ResourceNotFoundException {
        this.accountService.deposit(accountId, depositRequest.getAmount());
        return ResponseEntity.ok(new MessageResponseDTO(ApplicationConstants.DEPOSIT_SUCCESSFULLY.toString()));
    }

    @Operation(summary = "Get Balance by Account ID")
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_200, content = {@Content(schema = @Schema(implementation = Map.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_404, content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @GetMapping("/balance/{accountId}")
    public Map<String, Double> getBalance(@PathVariable @Positive @NotNull(message = "Amount cannot be null") Long accountId) throws ResourceNotFoundException {
        double balance = accountService.getBalance(accountId);
        Map<String, Double> response = new HashMap<>();
        response.put("balance", balance);
        return response;
    }
}
