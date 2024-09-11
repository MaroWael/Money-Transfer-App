package com.transfer.service;

import com.transfer.dto.FavoriteRecipientDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IFavoriteRecipientService {
    FavoriteRecipientDTO addFavoriteRecipient(String customerUsername, FavoriteRecipientDTO favoriteRecipientDTO) throws ResourceNotFoundException;

    List<FavoriteRecipientDTO> getFavoriteRecipients(String customerUsername) throws ResourceNotFoundException;

    @Transactional
    void deleteFavoriteRecipientByAccountNumber(String recipientAccountNumber) throws ResourceNotFoundException;
}
