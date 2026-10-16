package com.bank.repository;
import com.bank.domain.Account;
import  java.util.Map;
import  java.util.HashMap;
import  java.util.Optional;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepository {

    private  final Map<UUID, Account> accounts = new HashMap<>();
    
    @Override 
    public void  save(Account account){
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
