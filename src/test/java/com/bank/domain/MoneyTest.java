package com.bank.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithValidData() {
        Currency usd = Currency.getInstance("USD");

        Money money = new Money(
                new BigDecimal("100.00"),
                usd);

        assertEquals(
                0,
                new BigDecimal("100.00").compareTo(money.amount()));

        assertEquals(usd, money.currency());
    }

    @Test
    void shouldRejectNegativeAmount() {
        Currency usd = Currency.getInstance("USD");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(
                        new BigDecimal("-1"),
                        usd));
    }

    @Test
    void shouldAddMoneyWithSameCurrency() {
        Currency usd = Currency.getInstance("USD");

        Money first = new Money(
                new BigDecimal("10.00"),
                usd);

        Money second = new Money(
                new BigDecimal("5.00"),
                usd);

        Money result = first.add(second);

        assertEquals(
                0,
                new BigDecimal("15.00")
                        .compareTo(result.amount()));

        assertEquals(usd, result.currency());
    }

    @Test
    void shouldRejectAddingDifferentCurrencies() {
        Currency usd = Currency.getInstance("USD");
        Currency eur = Currency.getInstance("EUR");

        Money dollars = new Money(
                new BigDecimal("10"),
                usd);

        Money euros = new Money(
                new BigDecimal("5"),
                eur);

        assertThrows(
                IllegalArgumentException.class,
                () -> dollars.add(euros));
    }

    @Test
    void shouldRejectSubtractingMoreThanAvailableAmount() {
        Currency usd = Currency.getInstance("USD");

        Money money = new Money(
                new BigDecimal("10"),
                usd);

        Money amountToSubtract = new Money(
                new BigDecimal("15"),
                usd);

        assertThrows(
                IllegalArgumentException.class,
                () -> money.subtract(amountToSubtract));
    }

    @Test
    void shouldTreatDifferentBigDecimalScalesAsEqual() {
        Currency usd = Currency.getInstance("USD");

        Money first = new Money(
                new BigDecimal("10.0"),
                usd);

        Money second = new Money(
                new BigDecimal("10.00"),
                usd);

        assertEquals(first, second);
    }

    @Test
    void shouldSubtractMoneyWithSameCurrency() {
        Currency usd = Currency.getInstance("USD");

        Money money = new Money(
                new BigDecimal("10.00"),
                usd);

        Money amountToSubtract = new Money(
                new BigDecimal("3.50"),
                usd);

        Money result = money.subtract(amountToSubtract);

        assertEquals(
                0,
                new BigDecimal("6.50").compareTo(result.amount()));

        assertEquals(usd, result.currency());
    }
}