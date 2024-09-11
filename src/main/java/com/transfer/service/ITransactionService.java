package com.transfer.service;

import com.transfer.dto.TransactionRequestDTO;
import com.transfer.dto.TransactionResponseDTO;
import com.transfer.entity.Transaction;
import com.transfer.exception.custom.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITransactionService {
    @Transactional
    TransactionResponseDTO transferMoney(TransactionRequestDTO request) throws ResourceNotFoundException;

    List<Transaction> getTransactionHistory(Long accountId) throws ResourceNotFoundException;
}
