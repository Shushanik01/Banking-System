package com.bank.service;

import com.bank.domain.Account;
import com.bank.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @Test
    void shouldTransferMoneySuccessfully() {
        Currency usd = Currency.getInstance("USD");

        Account from = accountWith(usd, "100.00");
        Account to = accountWith(usd, "50.00");

        Money amount = new Money(
                new BigDecimal("30.00"),
                usd
        );

        AccountService service = new AccountService();

        service.transfer(from, to, amount);

        assertEquals(
                0,
                new BigDecimal("70.00")
                        .compareTo(from.balance().amount())
        );

        assertEquals(
                0,
                new BigDecimal("80.00")
                        .compareTo(to.balance().amount())
        );
    }

    @Test
    void shouldNotChangeBalancesWhenFundsAreInsufficient() {
        Currency usd = Currency.getInstance("USD");

        Account from = accountWith(usd, "20.00");
        Account to = accountWith(usd, "50.00");

        Money amount = new Money(
                new BigDecimal("30.00"),
                usd
        );

        AccountService service = new AccountService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(from, to, amount)
        );

        assertEquals(
                0,
                new BigDecimal("20.00")
                        .compareTo(from.balance().amount())
        );

        assertEquals(
                0,
                new BigDecimal("50.00")
                        .compareTo(to.balance().amount())
        );
    }

    @Test
    void shouldNotChangeBalancesWhenCurrenciesAreDifferent() {
        Currency usd = Currency.getInstance("USD");
        Currency eur = Currency.getInstance("EUR");

        Account from = accountWith(usd, "100.00");
        Account to = accountWith(eur, "50.00");

        Money amount = new Money(
                new BigDecimal("30.00"),
                usd
        );

        AccountService service = new AccountService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(from, to, amount)
        );

        assertEquals(
                0,
                new BigDecimal("100.00")
                        .compareTo(from.balance().amount())
        );

        assertEquals(
                0,
                new BigDecimal("50.00")
                        .compareTo(to.balance().amount())
        );
    }

    @Test
    void shouldRejectTransferToSameAccount() {
        Currency usd = Currency.getInstance("USD");

        Account account = accountWith(usd, "100.00");

        Money amount = new Money(
                new BigDecimal("30.00"),
                usd
        );

        AccountService service = new AccountService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(account, account, amount)
        );

        assertEquals(
                0,
                new BigDecimal("100.00")
                        .compareTo(account.balance().amount())
        );
    }

    private Account accountWith(Currency currency, String amount) {
        Account account = new Account(UUID.randomUUID(), currency);
        account.deposit(new Money(new BigDecimal(amount), currency));
        return account;
    }
}
