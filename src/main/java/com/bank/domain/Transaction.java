package com.bank.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Transaction(
        UUID id,
        TransactionType type,
        Money amount,
        Long fromAccountId,
        Long toAccountId,
        LocalDateTime createdAt
) {

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        TRANSFER
    }
}
