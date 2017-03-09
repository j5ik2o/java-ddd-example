package com.github.j5ik2o.ddd_example;

import org.junit.Test;

public class CustomerAccountTest {

    @Test
    public void test() {
        CustomerAccount ca = new CustomerAccount(1L, "test");
        System.out.println(ca.toString());
    }
}