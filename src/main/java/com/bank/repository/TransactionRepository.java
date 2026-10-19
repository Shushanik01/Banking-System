package com.bank.repository;
import com.bank.domain.Transaction;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository {
    
    void save(Transaction transaction);

    List<Transaction> findById(UUID accountId);

}
