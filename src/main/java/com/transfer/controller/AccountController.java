package com.transfer.controller;

import com.transfer.dto.AccountDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.response.ErrorDetails;
import com.transfer.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Validated
@Tag(name = "Account Controller", description = "Account controller")
public class AccountController {

    private final IAccountService accountService;

    @Operation(summary = "Get Account by Id")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/{accountId}")
    public AccountDTO getAccountById(@PathVariable Long accountId) throws ResourceNotFoundException {
        return this.accountService.getAccountById(accountId);
    }

    @Operation(summary = "Deposit Money")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/{accountId}/deposit")
    public void deposit(@PathVariable Long accountId, @RequestParam Double amount) throws ResourceNotFoundException {
        this.accountService.deposit(accountId, amount);
    }

    @Operation(summary = "Get Balance by Account ID")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Map.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/balance/{accountId}")
    public Map<String, Double> getBalance(@PathVariable Long accountId) throws ResourceNotFoundException {
        double balance = accountService.getBalance(accountId);
        Map<String, Double> response = new HashMap<>();
        response.put("balance", balance);
        return response;
    }
}
