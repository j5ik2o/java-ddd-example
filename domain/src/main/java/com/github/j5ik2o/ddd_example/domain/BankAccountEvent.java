package com.github.j5ik2o.ddd_example.domain;

import lombok.NonNull;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
public final class BankAccountEvent {
    @NonNull
    private Long id;
    private Long toBankAccountId;
    private Long fromBankAccountId;
    @NonNull
    private Money amount;
    @NonNull
    private ZonedDateTime occurredAt;

    public static BankAccountEvent of(Long id, Long toBankAccountId, Long fromBankAccountId, Money amount) {
        return new BankAccountEvent(id, toBankAccountId, fromBankAccountId, amount, ZonedDateTime.now());
    }
}
