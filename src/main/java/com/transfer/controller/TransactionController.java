package com.transfer.controller;

import com.transfer.constants.BusinessConstants;
import com.transfer.dto.TransactionRequestDTO;
import com.transfer.dto.TransactionResponseDTO;
import com.transfer.entity.Transaction;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.response.ErrorDetails;
import com.transfer.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class TransactionController {

    private final ITransactionService transactionService;

    @Operation(summary = "Transfer money between accounts")
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_200, content = {@Content(schema = @Schema(implementation = TransactionResponseDTO.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_400, content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transferMoney(@RequestBody @Valid TransactionRequestDTO request) throws ResourceNotFoundException {
        TransactionResponseDTO responseDTO = transactionService.transferMoney(request);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Get Transaction History by Account ID")
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_200, content = {@Content(schema = @Schema(implementation = TransactionResponseDTO.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @ApiResponse(responseCode = BusinessConstants.RESPONSE_CODE_400, content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = BusinessConstants.APPLICATION_JSON)})
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionHistory(@PathVariable @Positive Long accountId) throws ResourceNotFoundException {
        List<Transaction> transactions = transactionService.getTransactionHistory(accountId);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TransactionResponseDTO> responseDTOs = transactions.stream().map(transaction -> {
            TransactionResponseDTO responseDTO = new TransactionResponseDTO();
            responseDTO.setFromAccountNumber(transaction.getFromAccount().getAccountNumber());
            responseDTO.setToAccountNumber(transaction.getToAccount().getAccountNumber());
            responseDTO.setFromAccountName(transaction.getFromAccount().getCustomer().getName());
            responseDTO.setToAccountName(transaction.getToAccount().getCustomer().getName());
            responseDTO.setAmount(transaction.getAmount());
            responseDTO.setTransactionDate(transaction.getTransactionDate().toInstant().toString()); // Format to ISO 8601
            return responseDTO;
        }).toList();

        return ResponseEntity.ok(responseDTOs);
    }
}
