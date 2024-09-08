package com.transfer.repository;

import com.transfer.entity.Account;
import com.transfer.exception.custom.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.customer.email = :email")
    Optional<Account> findByCustomerUsername(@Param("email") String email);
    Optional<Account> findByAccountNumber(String accountNumber) throws ResourceNotFoundException;
}
