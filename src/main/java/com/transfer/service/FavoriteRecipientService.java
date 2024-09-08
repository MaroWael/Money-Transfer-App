package com.transfer.service;

import com.transfer.dto.FavoriteRecipientDTO;
import com.transfer.entity.Customer;
import com.transfer.entity.FavoriteRecipient;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.CustomerRepository;
import com.transfer.repository.FavoriteRecipientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteRecipientService {

    private final FavoriteRecipientRepository favoriteRecipientRepository;
    private final CustomerRepository customerRepository;

    public FavoriteRecipientDTO addFavoriteRecipient(String customerUsername, FavoriteRecipientDTO favoriteRecipientDTO) throws ResourceNotFoundException {
        Customer customer = customerRepository.findByEmail(customerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        FavoriteRecipient favoriteRecipient = FavoriteRecipient.builder()
                .recipientName(favoriteRecipientDTO.getRecipientName())
                .recipientAccountNumber(favoriteRecipientDTO.getRecipientAccountNumber())
                .customer(customer)
                .build();

        FavoriteRecipient savedRecipient = favoriteRecipientRepository.save(favoriteRecipient);

        return FavoriteRecipientDTO.builder()
                .recipientName(savedRecipient.getRecipientName())
                .recipientAccountNumber(savedRecipient.getRecipientAccountNumber())
                .build();
    }

    public List<FavoriteRecipientDTO> getFavoriteRecipients(String customerUsername) throws ResourceNotFoundException {
        Customer customer = customerRepository.findByEmail(customerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<FavoriteRecipient> favoriteRecipients = favoriteRecipientRepository.findByCustomerId(customer.getId());

        return favoriteRecipients.stream().map(recipient -> FavoriteRecipientDTO.builder()
                .recipientName(recipient.getRecipientName())
                .recipientAccountNumber(recipient.getRecipientAccountNumber())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoriteRecipientByAccountNumber(String recipientAccountNumber) throws ResourceNotFoundException {
        if (!favoriteRecipientRepository.findByRecipientAccountNumber(recipientAccountNumber).isPresent()) {
            throw new ResourceNotFoundException("Favorite recipient not found");
        }

        favoriteRecipientRepository.deleteByRecipientAccountNumber(recipientAccountNumber);
    }
}
