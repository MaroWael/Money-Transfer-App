package com.transfer.service;

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
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionResponseDTO transferMoney(TransactionRequestDTO request) throws ResourceNotFoundException {
        // Get the logged-in user's account
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();  // Assuming username is the unique identifier
        Account fromAccount = accountRepository.findByCustomerUsername(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));

        // Fetch receiver's account by account number
        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

        // Validate recipient name matches the receiver's account
        if (!toAccount.getCustomer().getName().equals(request.getRecipientName())) {
            throw new RuntimeException("Recipient name does not match the account");
        }

        // Check for sufficient balance
        if (fromAccount.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient funds in sender's account");
        }

        // Perform the transfer
        fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
        toAccount.setBalance(toAccount.getBalance() + request.getAmount());

        // Save the updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Save transaction history
        Transaction transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(request.getAmount())
                .recipientName(request.getRecipientName())
                .transactionDate(new Date()) // Set current date/time
                .build();
        transactionRepository.save(transaction);

        // Return response with a message
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setMessage("Transaction successful");
        return responseDTO;
    }

    public List<Transaction> getTransactionHistory(String accountNumber) throws ResourceNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return transactionRepository.findByFromAccountOrToAccount(account, account);
    }

}
