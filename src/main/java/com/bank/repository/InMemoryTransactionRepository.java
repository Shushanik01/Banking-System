package com.bank.repository;

import com.bank.domain.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findById(UUID accountId) {
        return transactions.stream()
                .filter(transaction -> {
                    return accountId.equals(transaction.fromAccountId())
                            || accountId.equals(transaction.toAccountId());
                })
                .toList();
    }
}
