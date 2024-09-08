package com.transfer.service;


import com.transfer.dto.AccountDTO;
import com.transfer.entity.Account;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    @Override
    public AccountDTO getAccountById(Long accountId) throws ResourceNotFoundException {
        return this.accountRepository.findById(accountId).orElseThrow(()
                -> new ResourceNotFoundException("Account not found")).toDTO();
    }

    @Override
    @Transactional
    public void deposit(Long accountId, Double amount) throws ResourceNotFoundException {
        Account account = this.accountRepository.findById(accountId).orElseThrow(() ->
                new ResourceNotFoundException("Account not found")
        );
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public double getBalance(Long accountId) throws ResourceNotFoundException {
        Account account = this.accountRepository.findById(accountId).orElseThrow(() ->
                new ResourceNotFoundException("Account not found")
        );
        return account.getBalance();
    }

}
