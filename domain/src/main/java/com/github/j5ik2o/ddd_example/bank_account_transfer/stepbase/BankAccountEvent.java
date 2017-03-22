package com.github.j5ik2o.ddd_example.bank_account_transfer.stepbase;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class BankAccountEvent {

    private Long id;

    private Long toBankAccountId;

    private Long fromBankAccountId;

    private Money money;

    private ZonedDateTime occurredAt;

}
