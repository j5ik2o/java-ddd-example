package com.github.j5ik2o.ddd_example.bank_account_transfer.stepbase;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

public class MoneyTest {

    @Test
    public void test() {

        Money money1 = new Money();
        money1.setCurrency(Currency.getInstance("JPY"));
        money1.setAmount(BigDecimal.valueOf(10000));

        Money money2 = new Money();
        money2.setCurrency(Currency.getInstance("JPY"));
        money2.setAmount(BigDecimal.valueOf(10000));

        BigDecimal total = money1.getAmount().add(money2.getAmount());

        Money result = new Money();
        result.setCurrency(money1.getCurrency());
        result.setAmount(total);

    }

}
