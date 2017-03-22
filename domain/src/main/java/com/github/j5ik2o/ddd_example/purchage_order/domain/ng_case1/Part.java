package com.github.j5ik2o.ddd_example.purchage_order.domain.ng_case1;

import lombok.NonNull;
import lombok.Value;

@Value
public final class Part {
    @NonNull
    private String name;
    @NonNull
    private long price;
}
