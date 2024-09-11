package com.transfer.service;

import com.transfer.dto.ChangePasswordDTO;
import com.transfer.dto.CustomerDTO;
import com.transfer.dto.UpdateCustomerDTO;
import com.transfer.entity.Customer;
import com.transfer.exception.custom.InvalidOldPasswordException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void testGetCustomerById_Success() throws ResourceNotFoundException {
        Customer customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .country("USA")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDTO result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("USA", result.getCountry());

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(1L));

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateCustomerProfile_Success() throws ResourceNotFoundException {
        Customer customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .country("USA")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        UpdateCustomerDTO updateCustomerDTO = UpdateCustomerDTO.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .country("Canada")
                .dateOfBirth(LocalDate.of(1985, 5, 5))
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.updateCustomerProfile(1L, updateCustomerDTO);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("Canada", result.getCountry());
        assertEquals(LocalDate.of(1985, 5, 5), result.getDateOfBirth());

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testUpdateCustomerProfile_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateCustomerDTO updateCustomerDTO = UpdateCustomerDTO.builder()
                .name("Jane Doe")
                .build();

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomerProfile(1L, updateCustomerDTO));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testChangeCustomerPassword_Success() throws ResourceNotFoundException, InvalidOldPasswordException {
        Customer customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .password("encodedOldPassword")
                .build();

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setNewPassword("newPassword");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        customerService.changeCustomerPassword(1L, changePasswordDTO);

        assertEquals("encodedNewPassword", customer.getPassword());

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testChangeCustomerPassword_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setNewPassword("newPassword");

        assertThrows(ResourceNotFoundException.class, () -> customerService.changeCustomerPassword(1L, changePasswordDTO));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testChangeCustomerPassword_InvalidOldPassword() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .password("encodedOldPassword")
                .build();

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("wrongOldPassword");
        changePasswordDTO.setNewPassword("newPassword");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("wrongOldPassword", "encodedOldPassword")).thenReturn(false);

        assertThrows(InvalidOldPasswordException.class, () -> customerService.changeCustomerPassword(1L, changePasswordDTO));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }
}
