package com.github.j5ik2o.ddd_example.bank_account_transfer.domain;

import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * お金を表す値オブジェクト。
 */
@Value
public final class Money {

    @NonNull
    private BigDecimal amount;

    @NonNull
    private Currency currency;


    public Money negated() {
        return of(amount.negate(), currency);
    }

    /**
     * お金を足す。
     *
     * @param other お金
     * @return お金
     */
    public Money add(Money other) {
        Validate.notNull(other);
        Validate.isTrue(currency.equals(other.currency));
        return of(amount.add(other.amount), currency);
    }

    /**
     * お金を引く。
     *
     * お金からお金を引くとお金の量が負になる可能性がある。
     *
     * @param other お金
     * @return お金
     */
    public Money substract(Money other) {
        return add(other.negated());
    }

    public boolean isGreaterThan(Money other) {
        return isGreaterThan(other.amount);
    }

    public boolean isGreaterThan(BigDecimal amount) {
        return this.amount.compareTo(amount) > 0;
    }

    public boolean isLessThan(Money other) {
        return isLessThan(other.amount);
    }

    public boolean isLessThan(BigDecimal amount) {
        return this.amount.compareTo(amount) < 0;
    }

    /**
     * 複数のお金を合計する。
     *
     * @param monies 複数のお金
     * @return 合計されたお金
     */
    public static Money sum(List<Money> monies) {
        if (monies.isEmpty()) {
            return zero(Money.DefaultCurrency);
        } else {
            Currency currency = monies.get(0).getCurrency();
            Money result = zero(currency);
            for (Money money : monies) {
                result = result.add(money);
            }
            return result;
        }
    }

    public static Money of(int amount) {
        return of(BigDecimal.valueOf(amount));
    }

    public static Money of(long amount) {
        return of(BigDecimal.valueOf(amount));
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money of(BigDecimal amount) {
        return of(amount, DefaultCurrency);
    }

    public static Money dollars(BigDecimal amount) {
        return new Money(amount, USD);
    }

    public static Money yens(BigDecimal amount) {
        return of(amount, JPY);
    }

    public static Money zero(Currency currency) {
        return of(BigDecimal.ZERO, currency);
    }

    public static Money zero() {
        return zero(DefaultCurrency);
    }

    public static Currency DefaultCurrency =
            Currency.getInstance(Locale.getDefault());

    public static Currency USD = Currency.getInstance("USD");
    public static Currency EUR = Currency.getInstance("EUR");
    public static Currency JPY = Currency.getInstance("JPY");

    public int DefaultRoundingMode = BigDecimal.ROUND_HALF_EVEN;
}
