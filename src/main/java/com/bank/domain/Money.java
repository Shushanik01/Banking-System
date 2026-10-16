package com.bank.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {
    
    public  Money {
        Objects.requireNonNull(amount, "Provide efficent amount");
        Objects.requireNonNull(currency, "Provide sufficient currency");

        if(amount.signum() < 0){
            throw new IllegalArgumentException("Amount can not be negative!");
        }
        amount = amount.stripTrailingZeros();
    }
    public  Money add(Money other){
        requireSameCurrency(other);
        return  new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other){
        requireSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if(result.signum() < 0) throw new IllegalArgumentException("Insufficient amount");
        return new Money(result, this.currency);
    }

    public boolean isGreaterThanOrEqual(Money other){
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

   private void requireSameCurrency(Money other){
        Objects.requireNonNull(other, "Money is required");
        if(!this.currency.equals(other.currency)){
            throw new IllegalArgumentException(
                "currency mismatch: " + this.currency + " vs " + other.currency
            );
        }
   }
}








