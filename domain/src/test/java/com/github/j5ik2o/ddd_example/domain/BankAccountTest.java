package com.github.j5ik2o.ddd_example.domain;

import com.github.j5ik2o.ddd_example.domain.BankAccount;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

public class BankAccountTest {

    @Test
    public void test() {
        BankAccount bankAccount = new BankAccount(1L, ImmutableList.of());
        System.out.println(bankAccount);
    }

}