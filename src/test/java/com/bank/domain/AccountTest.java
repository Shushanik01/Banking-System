package com.bank.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");

    @Test
    void shouldStartWithZeroBalance() {
        UUID id = UUID.randomUUID();
        Account account = new Account(id, USD);

        assertEquals(id, account.id());
        assertEquals(new Money(BigDecimal.ZERO, USD), account.balance());
    }

    @Test
    void shouldIncreaseBalanceOnDeposit() {
        Account account = new Account(UUID.randomUUID(), USD);

        account.deposit(new Money(new BigDecimal("10.00"), USD));

        assertEquals(new Money(new BigDecimal("10.00"), USD), account.balance());
    }

    @Test
    void shouldDecreaseBalanceOnWithdraw() {
        Account account = new Account(UUID.randomUUID(), USD);
        account.deposit(new Money(new BigDecimal("10.00"), USD));

        account.withdraw(new Money(new BigDecimal("3.50"), USD));

        assertEquals(new Money(new BigDecimal("6.50"), USD), account.balance());
    }

    @Test
    void shouldRejectDepositInDifferentCurrency() {
        Account account = new Account(UUID.randomUUID(), USD);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(new Money(new BigDecimal("5.00"), EUR)));
        assertEquals(new Money(BigDecimal.ZERO, USD), account.balance());
    }

    @Test
    void shouldRejectWithdrawAboveBalance() {
        Account account = new Account(UUID.randomUUID(), USD);
        account.deposit(new Money(new BigDecimal("10.00"), USD));

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(new Money(new BigDecimal("15.00"), USD)));
        assertEquals(new Money(new BigDecimal("10.00"), USD), account.balance());
    }

    @Test
    void shouldRejectZeroAmount() {
        Account account = new Account(UUID.randomUUID(), USD);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(new Money(BigDecimal.ZERO, USD)));
        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(new Money(BigDecimal.ZERO, USD)));
    }
}
