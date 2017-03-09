package com.github.j5ik2o.ddd_example.domain;

import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Value
public final class Money {

    @NonNull
    private BigDecimal amount;

    @NonNull
    private Currency currency;

    public Money dividedBy(BigDecimal divisor) {
        return dividedBy(divisor, DefaultRoundingMode);
    }

    public Money dividedBy(BigDecimal divisor, int roundingMode) {
        BigDecimal newAmount = amount.divide(divisor, roundingMode);
        return of(newAmount, currency);
    }

    public Money negated() {
        return of(amount.negate(), currency);
    }

    public Money plus(Money other) {
        Validate.notNull(other);
        Validate.isTrue(currency.equals(other.currency));
        return of(amount.add(other.amount), currency);
    }

    public Money minus(Money other) {
        return plus(other.negated());
    }

    public static Money sum(List<Money> monies) {
        if (monies.isEmpty()) {
            return zero(Money.DefaultCurrency);
        } else {
            Currency currency = monies.get(0).getCurrency();
            Money result = zero(currency);
            for (Money money : monies) {
                result = result.plus(money);
            }
            return result;
        }
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

    public static Currency DefaultCurrency =
            Currency.getInstance(Locale.getDefault());

    public static Currency USD = Currency.getInstance("USD");
    public static Currency EUR = Currency.getInstance("EUR");
    public static Currency JPY = Currency.getInstance("JPY");

    public int DefaultRoundingMode = BigDecimal.ROUND_HALF_EVEN;
}
