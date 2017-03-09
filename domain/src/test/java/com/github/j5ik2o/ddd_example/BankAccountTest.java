package com.github.j5ik2o.ddd_example;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;

public class BankAccountTest {

    @Test
    public void test() {
        BankAccount bankAccount = new BankAccount(1L, ImmutableList.of());
        System.out.println(bankAccount);
    }

}