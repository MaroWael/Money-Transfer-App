package com.transfer.service;

import com.transfer.dto.AccountDTO;
import com.transfer.entity.Account;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.AccountRepository;
import com.transfer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;  // The service to be tested

    @Mock
    private AccountRepository accountRepository;  // Mocked repository

    @Mock
    private CustomerRepository customerRepository;  // Mocked repository (even if not used in this test)


    @Test
    void testGetAccountById_Success() throws ResourceNotFoundException {
        // Mock Account entity
        Account account = new Account();
        account.setId(1L);
        account.setAccountName("Yousef");
        account.setBalance(1000.0);

        // Simulate repository returning the account
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Call the service method
        AccountDTO result = accountService.getAccountById(1L);

        // Verify the results
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Yousef", result.getAccountName());
        assertEquals(1000.0, result.getBalance());

        // Verify repository interaction
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAccountById_NotFound() {
        // Simulate repository returning empty
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test and expect ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(1L));

        // Verify repository interaction
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testDeposit_Success() throws ResourceNotFoundException {
        // Mock account entity
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);

        // Simulate repository finding the account
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Call the deposit method
        accountService.deposit(1L, 500.0);

        // Verify that the balance was updated and the account was saved
        assertEquals(1500.0, account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testDeposit_AccountNotFound() {
        // Simulate repository returning empty
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test and expect ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> accountService.deposit(1L, 500.0));

        // Verify repository interaction
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBalance_Success() throws ResourceNotFoundException {
        // Mock account entity
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);

        // Simulate repository returning the account
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Call the getBalance method
        double balance = accountService.getBalance(1L);

        // Verify the result
        assertEquals(1000.0, balance);
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBalance_AccountNotFound() {
        // Simulate repository returning empty
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test and expect ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> accountService.getBalance(1L));

        // Verify repository interaction
        verify(accountRepository, times(1)).findById(1L);
    }
}
