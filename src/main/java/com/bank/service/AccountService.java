package com.bank.service;

import com.bank.domain.Account;
import com.bank.domain.Money;

public class AccountService {

    public void transfer(Account from, Account to, Money amount) {
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
