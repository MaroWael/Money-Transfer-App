package com.transfer.service;

import com.transfer.constants.ApplicationConstants;
import com.transfer.dto.AccountDTO;
import com.transfer.entity.Account;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountDTO getAccountById(Long accountId) throws ResourceNotFoundException {
        return this.accountRepository.findById(accountId).orElseThrow(()
                -> new ResourceNotFoundException(ApplicationConstants.ACCOUNT_NOT_FOUND.toString()))
                .toDTO();
    }

    @Transactional
    @Override
    public void deposit(Long accountId, Double amount) throws ResourceNotFoundException {
        Account account = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.ACCOUNT_NOT_FOUND.toString()));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public double getBalance(Long accountId) throws ResourceNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.ACCOUNT_NOT_FOUND.toString()));
        return account.getBalance();
    }

}
