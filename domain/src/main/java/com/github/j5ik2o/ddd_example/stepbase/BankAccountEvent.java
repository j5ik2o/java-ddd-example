package com.github.j5ik2o.ddd_example.stepbase;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class BankAccountEvent {

    private Long id;

    private Long toBankAccountId;

    private Long fromBankAccountId;

    private Money amount;

    private ZonedDateTime occurredAt;

}
