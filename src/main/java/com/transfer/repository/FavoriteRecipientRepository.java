package com.transfer.repository;

import com.transfer.entity.Customer;
import com.transfer.entity.FavoriteRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRecipientRepository extends JpaRepository<FavoriteRecipient, Long> {
    boolean existsByRecipientAccountNumberAndCustomerEmail(String recipientAccountNumber, String customerEmail);
    List<FavoriteRecipient> findByCustomerId(Long customerId);
    Optional<FavoriteRecipient> findByRecipientAccountNumber(String recipientAccountNumber);
    void deleteByRecipientAccountNumber(String recipientAccountNumber);
}

