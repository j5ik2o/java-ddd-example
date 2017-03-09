package com.github.j5ik2o.ddd_example.domain;

import com.github.j5ik2o.ddd_eaxmple.utils.IdGenerator;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public final class BankAccount {

    @NonNull
    private Long id;

    @NonNull
    private List<BankAccountEvent> events;

    public BankAccount addEvent(BankAccountEvent event) {
        List<BankAccountEvent> result = Lists.newArrayList(this.events);
        result.add(event);
        return of(id, result);
    }

    public BankAccount addIncrementAmount(Money amount) {
        Long eventId = IdGenerator.generateId();
        return addEvent(BankAccountEvent.of(eventId, id, null, amount));
    }

    public BankAccount addDecrementAmount(Money amount) {
        return addIncrementAmount(amount.negated());
    }

    public BankAccount addIncrementEvent(BankAccount from, Money amount) {
        Long eventId = IdGenerator.generateId();
        return addEvent(BankAccountEvent.of(eventId, id, from.getId(), amount));
    }

    public BankAccount addDecrementEvent(BankAccount from, Money amount) {
        return addIncrementEvent(from, amount.negated());
    }

    private List<Money> getMonies() {
        List<Money> result = Lists.newArrayList();
        for (BankAccountEvent event : events) {
            result.add(event.getAmount());
        }
        return result;
    }

    public Money getTotalAmount() {
        return Money.sum(getMonies());
    }

    public static BankAccount of(Long id, List<BankAccountEvent> events) {
        return new BankAccount(id, events);
    }

    public static BankAccount of(Long id) {
        return of(id, Lists.newArrayList());
    }

}
