package com.github.j5ik2o.ddd_example;

import lombok.Value;

public final class BankAccountService {

    @Value
    public static class Result {
        private BankAccount to;
        private BankAccount from;
    }

    public static Result transfer(BankAccount to, BankAccount from, Money amount)  {
        Long decrementEventId = IdGenerator.generateId();
        BankAccountEvent fromDecrementEvent = BankAccountEvent.of(decrementEventId, to.getId(), from.getId(), amount.negated());
        Long incrementEventId = IdGenerator.generateId();
        BankAccountEvent toIncrementEvent = BankAccountEvent.of(incrementEventId, to.getId(), from.getId(), amount);
        return new Result(to.addEvent(toIncrementEvent),from.addEvent(fromDecrementEvent));
    }

}
