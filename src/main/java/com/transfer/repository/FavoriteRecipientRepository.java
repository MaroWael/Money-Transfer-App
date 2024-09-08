package com.transfer.repository;

import com.transfer.entity.FavoriteRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRecipientRepository extends JpaRepository<FavoriteRecipient, Long> {
    List<FavoriteRecipient> findByCustomerId(Long customerId);

    void deleteByRecipientAccountNumberAndCustomerEmail(String recipientAccountNumber, String customerEmail);

    Optional<FavoriteRecipient> findByRecipientAccountNumber(String recipientAccountNumber);

    void deleteByRecipientAccountNumber(String recipientAccountNumber);
}
