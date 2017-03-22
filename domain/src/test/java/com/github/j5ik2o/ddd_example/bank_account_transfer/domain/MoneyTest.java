package com.github.j5ik2o.ddd_example.bank_account_transfer.domain;

import org.junit.Test;

public class MoneyTest {

    @Test
    public void test() {
        Money money1 = Money.of(10000);
        Money money2 = Money.of(10000);
        Money result = money1.add(money2);
        if (result.isLessThan(Money.zero())) {
            System.out.println("result is less than zero!");
        }
        System.out.println(result);
    }

}