package com.transfer.service;

import com.transfer.dto.AccountDTO;
import com.transfer.exception.custom.ResourceNotFoundException;

public interface IAccountService {

    /**
     * Get account by id
     *
     * @param accountId the account id
     * @return the account
     * @throws ResourceNotFoundException if the account is not found
     */
    AccountDTO getAccountById(Long accountId) throws ResourceNotFoundException;

    void deposit(Long accountId, Double amount) throws ResourceNotFoundException;

    double getBalance(Long accountId) throws ResourceNotFoundException;

}
