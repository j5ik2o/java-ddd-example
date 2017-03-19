package com.github.j5ik2o.ddd_example.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class BankAccountEvent {
    @NonNull
    private Long id;
    private Long toBankAccountId;
    private Long fromBankAccountId;
    @NonNull
    private Money money;
    @NonNull
    private ZonedDateTime occurredAt;

    public static BankAccountEvent of(Long id, Long toBankAccountId, Long fromBankAccountId, Money money) {
        return new BankAccountEvent(id, toBankAccountId, fromBankAccountId, money, ZonedDateTime.now());
    }
}
