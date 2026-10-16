package com.bank.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public final class Account {

    private final UUID id;
    private Money balance;

    public Account(UUID id, Currency currency) {
        this.id = Objects.requireNonNull(id, "Account id is required");

        Currency validCurrency = Objects.requireNonNull(currency, "Currency is required");

        this.balance = new Money(BigDecimal.ZERO, validCurrency);
    }

    public void deposit(Money amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        validateAmount(amount);
        if (!this.balance.isGreaterThanOrEqual(amount)) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    public UUID id() {
        return this.id;
    }

    public Money balance() {
        return this.balance;
    }

    private void validateAmount(Money amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (!this.balance.currency().equals(amount.currency())) {
            throw new IllegalArgumentException("Currency is different");
        }

        if (amount.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
