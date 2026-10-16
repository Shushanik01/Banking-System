package com.bank.repository;

import com.bank.domain.Account;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    
    void save(Account account);

    Optional<Account> findById(UUID id);

    boolean existsById(UUID id);
}