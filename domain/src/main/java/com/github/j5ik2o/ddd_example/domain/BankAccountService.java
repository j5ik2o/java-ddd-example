package com.github.j5ik2o.ddd_example.domain;

import lombok.Value;

public final class BankAccountService {

    /**
     * 口座間送金後の状態を表す値オブジェクト。
     */
    @Value
    public static class TransferResult {
        private BankAccount to;
        private BankAccount from;
    }

    /**
     * 二つの口座間でお金を移動する。
     *
     * @param to 移動先の口座
     * @param from 移動元の口座
     * @param money お金
     * @return 移動後の結果
     */
    public static TransferResult transfer(BankAccount to, BankAccount from, Money money) {
        BankAccount updatedFrom = from.withdrawTo(to, money);
        BankAccount updatedTo = to.depositFrom(from, money);
        return new TransferResult(updatedTo, updatedFrom);
    }

}
