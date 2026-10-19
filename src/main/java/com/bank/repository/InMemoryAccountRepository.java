package com.bank.repository;
import com.bank.domain.Account;
import  java.util.Map;
import java.util.Currency;
import  java.util.HashMap;
import  java.util.Optional;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepository {

    private  final Map<UUID, Account> accounts = new HashMap<>();
    
    @Override 
    public Account openAccount(Currency currency){
        final UUID accountId = UUID.randomUUID();
        // private void validateCurrency(Currency currency){

        // }
        final Account newAccount = new Account(accountId, currency);
        accounts.put(accountId, newAccount);
        return newAccount;
    }

    @Override 
    public  void  save(Account account){
        accounts.put(account.id(), account);
    }

    @Override 
    public Optional<Account>findById(UUID id){
        return Optional.ofNullable(accounts.get(id));
    }

    @Override 
    public boolean existsById(UUID id){
        return  accounts.containsKey(id);
    }
}
