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

    private BankAccount addBankAccountEvent(Long toBankAccountId, Long fromBankAccountId, Money money) {
        Long eventId = IdGenerator.generateId();
        return addBankAccountEvent(BankAccountEvent.of(eventId, id, null, money));
    }

    private BankAccount addBankAccountEvent(BankAccountEvent event) {
        List<BankAccountEvent> result = Lists.newArrayList(events);
        result.add(event);
        return of(id, result);
    }

    /**
     * この口座に現金を預け入れる。
     *
     * @param money お金
     * @return 新しい状態の口座
     */
    public BankAccount depositCash(Money money) {
        return addBankAccountEvent(id, null, money);
    }

    /**
     * この口座から現金を引き出す。
     *
     * @param money お金
     * @return 新しい状態の口座
     */
    public BankAccount withdrawCash(Money money) {
        return addBankAccountEvent(null, id, money.negated());
    }

    /**
     * この口座に他の口座からお金を預け入れる。
     *
     * @param from 移動元の口座
     * @param money お金
     * @return 新しい状態の口座
     */
    public BankAccount depositFrom(BankAccount from, Money money) {
        return addBankAccountEvent(id, from.getId(), money);
    }

    /**
     * この口座から他の口座にお金を引き出す。
     *
     * @param to 移動先の口座
     * @param money お金
     * @return 新しい口座
     */
    public BankAccount withdrawTo(BankAccount to, Money money) {
        return addBankAccountEvent(to.getId(), id, money.negated());
    }

    /**
     * この口座の残高を返す。
     *
     * @return 残高
     */
    public Money getBalance() {
        return getBalanceByEvents(events);
    }

    public static BankAccount of(Long id, List<BankAccountEvent> events) {
        if (getBalanceByEvents(events).isLessThan(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("total money is less than zero!");
        }
        return new BankAccount(id, events);
    }

    public static BankAccount of(Long id) {
        return of(id, Lists.newArrayList());
    }

    private static Money getBalanceByEvents(List<BankAccountEvent> events) {
        return getTotalAmountByMonies(getMonies(events));
    }

    private static Money getTotalAmountByMonies(List<Money> monies) {
        return Money.sum(monies);
    }

    private static List<Money> getMonies(List<BankAccountEvent> events) {
        List<Money> result = Lists.newArrayList();
        for (BankAccountEvent event : events) {
            result.add(event.getMoney());
        }
        return result;
    }
}
