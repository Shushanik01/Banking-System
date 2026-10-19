package com.bank.repository;

import com.bank.domain.Account;
import java.util.Optional;
import java.util.UUID;
import java.util.Currency;


public interface AccountRepository {

    Account openAccount(Currency currency);

    void save(Account account);

    Optional<Account> findById(UUID id);

    boolean existsById(UUID id);
}