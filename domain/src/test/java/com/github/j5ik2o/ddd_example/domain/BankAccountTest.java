package com.github.j5ik2o.ddd_example.domain;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

public class BankAccountTest {

    @Test
    public void test() {
        BankAccount bankAccount = BankAccount.of(1L).depositCash(Money.of(10000));
        System.out.println(bankAccount.getBalance());
    }

}