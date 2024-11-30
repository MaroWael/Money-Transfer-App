package com.transfer.service;

import com.transfer.constants.ApplicationConstants;
import com.transfer.dto.TransactionRequestDTO;
import com.transfer.dto.TransactionResponseDTO;
import com.transfer.entity.Account;
import com.transfer.entity.Transaction;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public TransactionResponseDTO transferMoney(TransactionRequestDTO request) throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();  // Assuming username is the unique identifier
        Account fromAccount = accountRepository.findByCustomerUsername(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.SENDER_ACCOUNT_NOT_FOUND.toString()));

        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.RECEIVER_ACCOUNT_NOT_FOUND.toString()));

        if (!toAccount.getCustomer().getName().equals(request.getRecipientName())) {
            throw new RuntimeException(ApplicationConstants.ERROR_RECIPIENT_NAME.toString());
        }

        if (fromAccount.getBalance() < request.getAmount()) {
            throw new RuntimeException(ApplicationConstants.INSUFFICIENT_FUNDS.toString());
        }

        fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
        toAccount.setBalance(toAccount.getBalance() + request.getAmount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(request.getAmount())
                .recipientName(request.getRecipientName())
                .transactionDate(new Date())
                .build();
        transactionRepository.save(transaction);


        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setFromAccountNumber(fromAccount.getAccountNumber());
        responseDTO.setToAccountNumber(toAccount.getAccountNumber());
        responseDTO.setFromAccountName(fromAccount.getCustomer().getName());
        responseDTO.setToAccountName(toAccount.getCustomer().getName());
        responseDTO.setAmount(request.getAmount());
        responseDTO.setTransactionDate(transaction.getTransactionDate().toInstant().toString()); // Format to ISO 8601
        return responseDTO;
    }

    @Override
    public List<Transaction> getTransactionHistory(Long accountId) throws ResourceNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.ACCOUNT_NOT_FOUND.toString()));

        return transactionRepository.findByFromAccountOrToAccount(account, account);
    }
}