package com.github.j5ik2o.ddd_example.stepbase;

import com.github.j5ik2o.ddd_eaxmple.utils.IdGenerator;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class BankAccountService {
    @Data
    public static class Result {
        private BankAccount to;
        private BankAccount from;
    }

    public static Result moveData(BankAccount to, BankAccount from, Money money) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(BankAccountEvent event : to.getEvents()) {
            totalAmount = totalAmount.add(event.getMoney().getAmount());
        }
        if (totalAmount.compareTo(money.getAmount()) < 0) {
            throw new IllegalArgumentException("total money is less than zero!");
        }
        BankAccountEvent decrementEvent = new BankAccountEvent();
        decrementEvent.setId(IdGenerator.generateId());
        decrementEvent.setFromBankAccountId(from.getId());
        decrementEvent.setToBankAccountId(to.getId());
        Money negated = new Money();
        negated.setCurrency(money.getCurrency());
        negated.setAmount(money.getAmount().negate());
        decrementEvent.setMoney(negated);
        decrementEvent.setOccurredAt(ZonedDateTime.now());
        BankAccount newFrom = new BankAccount();
        List<BankAccountEvent> newFromEvent = Lists.newArrayList(from.getEvents());
        newFromEvent.add(decrementEvent);
        newFrom.setId(from.getId());
        newFrom.setEvents(newFromEvent);

        BankAccountEvent incrementEvent = new BankAccountEvent();
        incrementEvent.setId(IdGenerator.generateId());
        incrementEvent.setFromBankAccountId(from.getId());
        incrementEvent.setToBankAccountId(to.getId());
        incrementEvent.setMoney(money);
        incrementEvent.setOccurredAt(ZonedDateTime.now());
        BankAccount newTo = new BankAccount();
        List<BankAccountEvent> newToEvent = Lists.newArrayList(to.getEvents());
        newToEvent.add(incrementEvent);
        newTo.setId(to.getId());
        newTo.setEvents(newToEvent);

        Result result = new Result();
        result.setFrom(newFrom);
        result.setTo(newTo);
        return result;

    }

}
