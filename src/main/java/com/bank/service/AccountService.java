package com.bank.service;

import com.bank.domain.Account;
import com.bank.domain.Money;
import java.util.Currency;
import  java.util.UUID;

import com.bank.repository.AccountRepository;

public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository){
        this.repository = repository;
    }

    public Account openAccount(Currency currency){
        final UUID accountId = UUID.randomUUID();
        // private void validateCurrency(Currency currency){

        // }
        final Account newAccount = new Account(accountId, currency);
        // accounts.put(accountId, newAccount);
        repository.save(newAccount);
        return newAccount;
    }

    public void deposit(UUID accountId, Money amount ){
        Account account = repository.findById(accountId)
        .orElseThrow(()-> new IllegalArgumentException("Account not found"));

        account.deposit(amount);
        repository.save(account);
    }

    public void withdraw(UUID accountId, Money amount){
        Account account = repository.findById(accountId)
        .orElseThrow(()-> new IllegalArgumentException("Account not found"));

        account.withdraw(amount);
        repository.save(account);
    }

    public void transfer(UUID fromId, UUID toId, Money amount){

        Account toAccount = repository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Account to transfer to not found"));
        
        Account fromAccount = repository.findById(fromId)
                .orElseThrow(()-> new IllegalArgumentException("Account to transfer from not found"));

        transfer(fromAccount, toAccount, amount);
        repository.save(fromAccount);
        repository.save(toAccount);
    }

    private void transfer(Account from, Account to, Money amount) {
        validateTransfer(from, to, amount);

        from.withdraw(amount);
        to.deposit(amount);
    }

    private void validateTransfer(Account from, Account to, Money amount) {

        if(from == null || to == null || amount == null){
            throw new IllegalArgumentException("Transfer arguments can not be null");
        }
        if(from == to) 
            throw new IllegalArgumentException("Can nt transfer to the same account");
        if(!to.balance().currency().equals(amount.currency()))
            throw new IllegalArgumentException("Currency is different");
        if (!from.balance().currency().equals(amount.currency())) {
            throw new IllegalArgumentException("Source account currency mismatch");
        }
    }
}
