package com.github.j5ik2o.ddd_example.domain;

import lombok.Value;

public final class BankAccountService {

    @Value
    public static class Result {
        private BankAccount to;
        private BankAccount from;
    }

    public static Result transfer(BankAccount to, BankAccount from, Money amount) {
        BankAccount updatedFrom = from.withdrawTo(to, amount);
        BankAccount updatedTo = to.depositFrom(from, amount);
        return new Result(updatedTo, updatedFrom);
    }

}
