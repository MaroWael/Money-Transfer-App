package com.transfer.service;

import com.transfer.constants.ApplicationConstants;
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
public class FavoriteRecipientService implements IFavoriteRecipientService {

    private final FavoriteRecipientRepository favoriteRecipientRepository;
    private final CustomerRepository customerRepository;

    @Override
    public FavoriteRecipientDTO addFavoriteRecipient(String customerUsername, FavoriteRecipientDTO favoriteRecipientDTO) throws ResourceNotFoundException {
        Customer customer = customerRepository.findByEmail(customerUsername)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.CUSTOMER_NOT_FOUND.toString()));

        if (favoriteRecipientRepository.existsByRecipientAccountNumberAndCustomerEmail(
                favoriteRecipientDTO.getRecipientAccountNumber(), customerUsername)) {
            throw new ResourceNotFoundException(ApplicationConstants.FAVORITE_RECIPIENT_ALREADY_EXIST.toString());
        }

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

    @Override
    public List<FavoriteRecipientDTO> getFavoriteRecipients(String customerUsername) throws ResourceNotFoundException {
        Customer customer = customerRepository.findByEmail(customerUsername)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.CUSTOMER_NOT_FOUND.toString()));

        List<FavoriteRecipient> favoriteRecipients = favoriteRecipientRepository.findByCustomerId(customer.getId());

        return favoriteRecipients.stream().map(recipient -> FavoriteRecipientDTO.builder()
                .recipientName(recipient.getRecipientName())
                .recipientAccountNumber(recipient.getRecipientAccountNumber())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteFavoriteRecipientByAccountNumber(String recipientAccountNumber) throws ResourceNotFoundException {
        FavoriteRecipient recipient = favoriteRecipientRepository.findByRecipientAccountNumber(recipientAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.FAVORITE_RECIPIENT_NOT_FOUND.toString()));

        favoriteRecipientRepository.delete(recipient);
    }
}
