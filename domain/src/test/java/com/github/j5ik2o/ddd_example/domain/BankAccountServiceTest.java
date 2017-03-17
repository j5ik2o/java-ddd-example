package com.github.j5ik2o.ddd_example.domain;

import com.github.j5ik2o.ddd_eaxmple.utils.IdGenerator;
import org.junit.Test;

import java.math.BigDecimal;

public class BankAccountServiceTest {
    @Test
    public void transfer1() throws Exception {
        Money baseMoney = Money.of(BigDecimal.valueOf(10000));
        // 口座を宣言し、初期入金を行う
        BankAccount bankAccount1 = BankAccount.of(IdGenerator.generateId())
                .depositCash(baseMoney);
        BankAccount bankAccount2 = BankAccount.of(IdGenerator.generateId())
                .depositCash(baseMoney);

        System.out.println("bankAccount1 = " + bankAccount1.getTotalAmount());
        System.out.println("bankAccount2 = " + bankAccount2.getTotalAmount());

        // 口座間送金を行う
        BankAccountService.Result result = BankAccountService.transfer(
                bankAccount1,
                bankAccount2,
                Money.of(BigDecimal.valueOf(10000))
        );

        System.out.println("to = " + result.getTo().getTotalAmount());
        System.out.println("from = " + result.getFrom().getTotalAmount());
    }

    @Test
    public void transfer2() throws Exception {
        Money baseMoney = Money.of(BigDecimal.valueOf(10000));
        // 口座を宣言し、初期入金を行う
        BankAccount bankAccount1 = BankAccount.of(IdGenerator.generateId())
                .depositCash(baseMoney);
        BankAccount bankAccount2 = BankAccount.of(IdGenerator.generateId())
                .depositCash(baseMoney);

        System.out.println("bankAccount1 = " + bankAccount1.getTotalAmount());
        System.out.println("bankAccount2 = " + bankAccount2.getTotalAmount());

        // 口座間送金を行う
        BankAccountService.Result result = BankAccountService.transfer(
                bankAccount1,
                bankAccount2,
                Money.of(BigDecimal.valueOf(20000))
        );

        System.out.println("to = " + result.getTo().getTotalAmount());
        System.out.println("from = " + result.getFrom().getTotalAmount());
    }
}
