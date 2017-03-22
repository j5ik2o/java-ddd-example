package com.github.j5ik2o.ddd_example.bank_account_transfer.domain;

import com.github.j5ik2o.ddd_eaxmple.utils.IdGenerator;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class BankAccount {

    @NonNull
    private Long id;

    @NonNull
    private List<BankAccountEvent> events;

    @NonNull
    public Long getId() {
        return this.id;
    }

    @NonNull
    public List<BankAccountEvent> getEvents() {
        return cloneList(this.events);
    }

    private BankAccount(Long id, List<BankAccountEvent> events) {
        Validate.notNull(id);
        Validate.notNull(events);
        this.id = id;
        if (getBalanceByEvents(events).isLessThan(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("total money is less than zero!");
        }
        this.events = cloneList(events);
    }

    private List<BankAccountEvent> cloneList(List<BankAccountEvent> events) {
        ArrayList<BankAccountEvent> result = Lists.newArrayListWithCapacity(events.size());
        for (BankAccountEvent event : events) {
            result.add(event);
        }
        return result;
    }

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
     * @param from  移動元の口座
     * @param money お金
     * @return 新しい状態の口座
     */
    public BankAccount depositFrom(BankAccount from, Money money) {
        return addBankAccountEvent(id, from.getId(), money);
    }

    /**
     * この口座から他の口座にお金を引き出す。
     *
     * @param to    移動先の口座
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BankAccount)) return false;
        final BankAccount other = (BankAccount) o;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$events = this.getEvents();
        final Object other$events = other.getEvents();
        if (this$events == null ? other$events != null : !this$events.equals(other$events)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $events = this.getEvents();
        result = result * PRIME + ($events == null ? 43 : $events.hashCode());
        return result;
    }

    public String toString() {
        return "com.github.j5ik2o.ddd_example.bank_account_transfer.domain.BankAccount(id=" + this.getId() + ", events=" + this.getEvents() + ")";
    }
}
