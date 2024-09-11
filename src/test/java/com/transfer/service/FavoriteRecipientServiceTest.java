package com.transfer.service;

import com.transfer.dto.FavoriteRecipientDTO;
import com.transfer.entity.Customer;
import com.transfer.entity.FavoriteRecipient;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.CustomerRepository;
import com.transfer.repository.FavoriteRecipientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteRecipientServiceTest {

    @InjectMocks
    private FavoriteRecipientService favoriteRecipientService;

    @Mock
    private FavoriteRecipientRepository favoriteRecipientRepository;

    @Mock
    private CustomerRepository customerRepository;


    @Test
    void testAddFavoriteRecipient_Success() throws ResourceNotFoundException {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@example.com");

        FavoriteRecipientDTO favoriteRecipientDTO = FavoriteRecipientDTO.builder()
                .recipientName("John Doe")
                .recipientAccountNumber("123456789")
                .build();

        when(customerRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));
        when(favoriteRecipientRepository.existsByRecipientAccountNumberAndCustomerEmail(anyString(), anyString()))
                .thenReturn(false);

        FavoriteRecipient savedRecipient = FavoriteRecipient.builder()
                .recipientName(favoriteRecipientDTO.getRecipientName())
                .recipientAccountNumber(favoriteRecipientDTO.getRecipientAccountNumber())
                .customer(customer)
                .build();
        when(favoriteRecipientRepository.save(any(FavoriteRecipient.class))).thenReturn(savedRecipient);

        FavoriteRecipientDTO result = favoriteRecipientService.addFavoriteRecipient("customer@example.com", favoriteRecipientDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getRecipientName());
        assertEquals("123456789", result.getRecipientAccountNumber());

        verify(customerRepository, times(1)).findByEmail("customer@example.com");
        verify(favoriteRecipientRepository, times(1)).save(any(FavoriteRecipient.class));
    }

    @Test
    void testAddFavoriteRecipient_CustomerNotFound() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        FavoriteRecipientDTO favoriteRecipientDTO = FavoriteRecipientDTO.builder()
                .recipientName("John Doe")
                .recipientAccountNumber("123456789")
                .build();

        assertThrows(ResourceNotFoundException.class, () ->
                favoriteRecipientService.addFavoriteRecipient("customer@example.com", favoriteRecipientDTO));

        verify(customerRepository, times(1)).findByEmail("customer@example.com");
        verify(favoriteRecipientRepository, never()).save(any(FavoriteRecipient.class));
    }

    @Test
    void testGetFavoriteRecipients_Success() throws ResourceNotFoundException {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@example.com");

        when(customerRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));

        List<FavoriteRecipient> favoriteRecipients = Arrays.asList(
                FavoriteRecipient.builder()
                        .recipientName("John Doe")
                        .recipientAccountNumber("123456789")
                        .customer(customer)
                        .build(),
                FavoriteRecipient.builder()
                        .recipientName("Jane Smith")
                        .recipientAccountNumber("987654321")
                        .customer(customer)
                        .build()
        );

        when(favoriteRecipientRepository.findByCustomerId(1L)).thenReturn(favoriteRecipients);

        List<FavoriteRecipientDTO> result = favoriteRecipientService.getFavoriteRecipients("customer@example.com");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getRecipientName());
        assertEquals("123456789", result.get(0).getRecipientAccountNumber());
        assertEquals("Jane Smith", result.get(1).getRecipientName());
        assertEquals("987654321", result.get(1).getRecipientAccountNumber());

        verify(customerRepository, times(1)).findByEmail("customer@example.com");
        verify(favoriteRecipientRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void testDeleteFavoriteRecipientByAccountNumber_Success() throws ResourceNotFoundException {
        FavoriteRecipient recipient = FavoriteRecipient.builder()
                .recipientName("John Doe")
                .recipientAccountNumber("123456789")
                .build();

        when(favoriteRecipientRepository.findByRecipientAccountNumber("123456789")).thenReturn(Optional.of(recipient));

        favoriteRecipientService.deleteFavoriteRecipientByAccountNumber("123456789");

        verify(favoriteRecipientRepository, times(1)).delete(recipient);
    }

    @Test
    void testDeleteFavoriteRecipientByAccountNumber_NotFound() {
        when(favoriteRecipientRepository.findByRecipientAccountNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                favoriteRecipientService.deleteFavoriteRecipientByAccountNumber("123456789"));

        verify(favoriteRecipientRepository, times(1)).findByRecipientAccountNumber("123456789");
        verify(favoriteRecipientRepository, never()).delete(any(FavoriteRecipient.class));
    }
}
