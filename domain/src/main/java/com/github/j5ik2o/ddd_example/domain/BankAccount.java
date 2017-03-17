package com.github.j5ik2o.ddd_example.domain;

import com.github.j5ik2o.ddd_eaxmple.utils.IdGenerator;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
public final class BankAccount {

    @NonNull
    private Long id;

    @NonNull
    private List<BankAccountEvent> events;

    private BankAccount addBankAccountEvent(Long toBankAccountId, Long fromBankAccountId, Money amount) {
        Long eventId = IdGenerator.generateId();
        return addBankAccountEvent(BankAccountEvent.of(eventId, id, null, amount));
    }

    private BankAccount addBankAccountEvent(BankAccountEvent event) {
        List<BankAccountEvent> result = Lists.newArrayList(events);
        result.add(event);
        return of(id, result);
    }

    public BankAccount depositCash(Money amount) {
        return addBankAccountEvent(id, null, amount);
    }

    public BankAccount withdrawCash(Money amount) {
        return addBankAccountEvent(null, id, amount.negated());
    }

    public BankAccount depositFrom(BankAccount from, Money amount) {
        return addBankAccountEvent(id, from.getId(), amount);
    }

    public BankAccount withdrawTo(BankAccount to, Money amount) {
        return addBankAccountEvent(to.getId(), id, amount.negated());
    }

    public Money getTotalAmount() {
        return getTotalAmountByEvents(events);
    }

    public static BankAccount of(Long id, List<BankAccountEvent> events) {
        if (getTotalAmountByEvents(events).isLessThan(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("total amount is less than zero!");
        }
        return new BankAccount(id, events);
    }

    public static BankAccount of(Long id) {
        return of(id, Lists.newArrayList());
    }

    private static Money getTotalAmountByEvents(List<BankAccountEvent> events) {
        return getTotalAmountByMonies(getMonies(events));
    }

    private static Money getTotalAmountByMonies(List<Money> monies) {
        return Money.sum(monies);
    }

    private static List<Money> getMonies(List<BankAccountEvent> events) {
        List<Money> result = Lists.newArrayList();
        for (BankAccountEvent event : events) {
            result.add(event.getAmount());
        }
        return result;
    }
}
