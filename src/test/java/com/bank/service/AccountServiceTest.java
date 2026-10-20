package com.bank.service;

import com.bank.domain.Account;
import com.bank.domain.Money;
import com.bank.domain.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.InMemoryAccountRepository;
import com.bank.repository.InMemoryTransactionRepository;
import com.bank.repository.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");

    private final AccountRepository accounts = new InMemoryAccountRepository();
    private final TransactionRepository transactions = new InMemoryTransactionRepository();
    private final AccountService service = new AccountService(accounts, transactions);

    @Test
    void shouldTransferMoneySuccessfully() {
        Account from = fundedAccount(USD, "100.00");
        Account to = fundedAccount(USD, "50.00");

        service.transfer(from.id(), to.id(), money("30.00", USD));

        assertEquals(money("70.00", USD), service.getAccount(from.id()).balance());
        assertEquals(money("80.00", USD), service.getAccount(to.id()).balance());
    }

    @Test
    void shouldNotChangeBalancesWhenFundsAreInsufficient() {
        Account from = fundedAccount(USD, "20.00");
        Account to = fundedAccount(USD, "50.00");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(from.id(), to.id(), money("30.00", USD)));

        assertEquals(money("20.00", USD), service.getAccount(from.id()).balance());
        assertEquals(money("50.00", USD), service.getAccount(to.id()).balance());
        assertEquals(1, service.getTransactions(from.id()).size());
    }

    @Test
    void shouldNotChangeBalancesWhenCurrenciesAreDifferent() {
        Account from = fundedAccount(USD, "100.00");
        Account to = fundedAccount(EUR, "50.00");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(from.id(), to.id(), money("30.00", USD)));

        assertEquals(money("100.00", USD), service.getAccount(from.id()).balance());
        assertEquals(money("50.00", EUR), service.getAccount(to.id()).balance());
    }

    @Test
    void shouldRejectTransferToSameAccount() {
        Account account = fundedAccount(USD, "100.00");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(account.id(), account.id(), money("30.00", USD)));

        assertEquals(money("100.00", USD), service.getAccount(account.id()).balance());
    }

    @Test
    void shouldRecordDeposit() {
        Account account = service.openAccount(USD);

        service.deposit(account.id(), money("10.00", USD));

        List<Transaction> history = service.getTransactions(account.id());
        assertEquals(1, history.size());
        Transaction recorded = history.getFirst();
        assertEquals(Transaction.TransactionType.DEPOSIT, recorded.type());
        assertEquals(money("10.00", USD), recorded.amount());
        assertNull(recorded.fromAccountId());
        assertEquals(account.id(), recorded.toAccountId());
    }

    @Test
    void shouldNotRecordWithdrawWhenFundsAreInsufficient() {
        Account account = fundedAccount(USD, "10.00");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.withdraw(account.id(), money("15.00", USD)));

        assertEquals(money("10.00", USD), service.getAccount(account.id()).balance());
        List<Transaction> history = service.getTransactions(account.id());
        assertEquals(1, history.size());
        assertEquals(Transaction.TransactionType.DEPOSIT, history.getFirst().type());
    }

    @Test
    void shouldRecordTransferForBothAccounts() {
        Account from = fundedAccount(USD, "100.00");
        Account to = fundedAccount(USD, "50.00");

        service.transfer(from.id(), to.id(), money("30.00", USD));

        assertEquals(Transaction.TransactionType.TRANSFER, lastTransaction(from).type());
        assertEquals(from.id(), lastTransaction(from).fromAccountId());
        assertEquals(to.id(), lastTransaction(from).toAccountId());
        assertEquals(lastTransaction(from), lastTransaction(to));
    }

    private Account fundedAccount(Currency currency, String amount) {
        Account account = service.openAccount(currency);
        service.deposit(account.id(), money(amount, currency));
        return account;
    }

    private Transaction lastTransaction(Account account) {
        List<Transaction> history = service.getTransactions(account.id());
        return history.getLast();
    }

    private Money money(String amount, Currency currency) {
        return new Money(new BigDecimal(amount), currency);
    }
}
