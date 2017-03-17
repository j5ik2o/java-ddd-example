package com.github.j5ik2o.ddd_example.stepbase;

import com.github.j5ik2o.ddd_eaxmple.utils.IdGenerator;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.List;

public class BankAccountServiceTest {

    public Money getTotal(BankAccount bankAccount){
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(BankAccountEvent event : bankAccount.getEvents()) {
            totalAmount = totalAmount.add(event.getMoney().getAmount());
        }
        Money money = new Money();
        money.setCurrency(bankAccount.getEvents().get(0).getMoney().getCurrency());
        money.setAmount(totalAmount);
        return money;
    }

    @Test
    public void transfer1() throws Exception {
        Money baseMoney = new Money();
        baseMoney.setCurrency(Currency.getInstance("JPY"));
        baseMoney.setAmount(BigDecimal.valueOf(10000));
        // 口座を宣言し、初期入金を行う
        BankAccount bankAccount1 = new BankAccount();
        bankAccount1.setId(IdGenerator.generateId());
        List<BankAccountEvent> events1 = Lists.newArrayList();
        BankAccountEvent incrementEvent1 = new BankAccountEvent();
        incrementEvent1.setId(IdGenerator.generateId());
        incrementEvent1.setToBankAccountId(bankAccount1.getId());
        incrementEvent1.setFromBankAccountId(null);
        incrementEvent1.setMoney(baseMoney);
        incrementEvent1.setOccurredAt(ZonedDateTime.now());
        events1.add(incrementEvent1);
        bankAccount1.setEvents(events1);


        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setId(IdGenerator.generateId());
        List<BankAccountEvent> events2 = Lists.newArrayList();
        BankAccountEvent incrementEvent2 = new BankAccountEvent();
        incrementEvent2.setId(IdGenerator.generateId());
        incrementEvent2.setToBankAccountId(bankAccount1.getId());
        incrementEvent2.setFromBankAccountId(null);
        incrementEvent2.setMoney(baseMoney);
        incrementEvent2.setOccurredAt(ZonedDateTime.now());
        events2.add(incrementEvent2);
        bankAccount2.setEvents(events2);

        Money totalAmount1 = getTotal(bankAccount1);
        Money totalAmount2 = getTotal(bankAccount2);

        System.out.println("bankAccount1 = " + totalAmount1);
        System.out.println("bankAccount2 = " + totalAmount2);

        Money data = new Money();
        data.setCurrency(bankAccount1.getEvents().get(0).getMoney().getCurrency());
        data.setAmount(BigDecimal.valueOf(10000));
        // 口座間送金を行う
        BankAccountService.Result result = BankAccountService.moveData(
                bankAccount1,
                bankAccount2,
                data
        );

        Money newTotalAmount1 = getTotal(result.getTo());
        Money newTotalAmount2 = getTotal(result.getFrom());

        System.out.println("toTotalAmount = " + newTotalAmount1);
        System.out.println("fromTotalAmount = " + newTotalAmount2);
    }

}
