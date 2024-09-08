package com.transfer.repository;

import com.transfer.entity.Account;
import com.transfer.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount);
}
