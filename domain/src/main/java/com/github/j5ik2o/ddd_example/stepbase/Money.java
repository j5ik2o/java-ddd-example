package com.github.j5ik2o.ddd_example.stepbase;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
public class Money {

    private BigDecimal amount;

    private Currency currency;

}
