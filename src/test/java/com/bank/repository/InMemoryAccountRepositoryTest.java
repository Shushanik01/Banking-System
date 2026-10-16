package com.bank.repository;

import com.bank.domain.Account;
import com.bank.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryAccountRepositoryTest {

    private static final Currency USD = Currency.getInstance("USD");

    private final AccountRepository repository = new InMemoryAccountRepository();

    @Test
    void shouldFindSavedAccount() {
        Account account = accountWith("10.00");

        repository.save(account);

        assertTrue(repository.existsById(account.id()));
        assertEquals(account, repository.findById(account.id()).orElseThrow());
    }

    @Test
    void shouldReturnEmptyForUnknownId() {
        UUID id = UUID.randomUUID();

        assertFalse(repository.existsById(id));
        assertTrue(repository.findById(id).isEmpty());
    }

    @Test
    void shouldReplaceAccountWithSameId() {
        UUID id = UUID.randomUUID();
        Account original = new Account(id, USD);
        original.deposit(new Money(new BigDecimal("10.00"), USD));
        Account replacement = new Account(id, USD);
        replacement.deposit(new Money(new BigDecimal("25.00"), USD));

        repository.save(original);
        repository.save(replacement);

        Account found = repository.findById(id).orElseThrow();
        assertEquals(new Money(new BigDecimal("25.00"), USD), found.balance());
    }

    private Account accountWith(String amount) {
        Account account = new Account(UUID.randomUUID(), USD);
        account.deposit(new Money(new BigDecimal(amount), USD));
        return account;
    }
}
