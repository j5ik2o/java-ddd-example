package com.github.j5ik2o.ddd_example.bank_account_transfer.stepbase;

import lombok.Data;

import java.util.List;

@Data
public class BankAccount {

    private Long id;

    private List<BankAccountEvent> events;

}
