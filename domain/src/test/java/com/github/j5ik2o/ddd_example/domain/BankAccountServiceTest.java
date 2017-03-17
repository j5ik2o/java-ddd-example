package com.github.j5ik2o.ddd_example.domain;

import com.github.j5ik2o.ddd_eaxmple.utils.IdGenerator;
import org.junit.Test;

public class BankAccountServiceTest {

    @Test
    public void transfer1() throws Exception {
        Money initialMoney = Money.of(10000);
        BankAccount bankAccount1 = BankAccount.of(IdGenerator.generateId()).depositCash(initialMoney);
        BankAccount bankAccount2 = BankAccount.of(IdGenerator.generateId()).depositCash(initialMoney);

        System.out.println("bankAccount1 = " + bankAccount1.getBalance());
        System.out.println("bankAccount2 = " + bankAccount2.getBalance());

        BankAccountService.TransferResult transferResult = BankAccountService.transfer(
                bankAccount1,
                bankAccount2,
                Money.of(10000)
        );

        System.out.println("to = " + transferResult.getTo().getBalance());
        System.out.println("from = " + transferResult.getFrom().getBalance());
    }

    @Test
    public void transfer2() throws Exception {
        Money initialMoney = Money.of(10000);
        BankAccount bankAccount1 = BankAccount.of(IdGenerator.generateId()).depositCash(initialMoney);
        BankAccount bankAccount2 = BankAccount.of(IdGenerator.generateId()).depositCash(initialMoney);

        System.out.println("bankAccount1 = " + bankAccount1.getBalance());
        System.out.println("bankAccount2 = " + bankAccount2.getBalance());

        BankAccountService.TransferResult transferResult = BankAccountService.transfer(
                bankAccount1,
                bankAccount2,
                Money.of(20000)
        );

        System.out.println("to = " + transferResult.getTo().getBalance());
        System.out.println("from = " + transferResult.getFrom().getBalance());
    }
}
