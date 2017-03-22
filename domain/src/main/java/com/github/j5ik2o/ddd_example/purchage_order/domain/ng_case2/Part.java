package com.github.j5ik2o.ddd_example.purchage_order.domain.ng_case2;

import lombok.NonNull;
import org.apache.commons.lang3.Validate;

public final class Part {
    @NonNull
    private final String name;

    @NonNull
    private long price;

    private boolean updated;

    private boolean editing;

    public Part(String name, long price, boolean updated, boolean editing) {
        Validate.notNull(name);
        this.name = name;
        this.price = price;
        this.updated = updated;
        this.editing = editing;
    }

    public Part(String name, long price) {
        this(name, price, false, false);
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public long getPrice() {
        return this.price;
    }

    public Part withPrice(long price) {
        return new Part(name, price, true, editing);
    }

    public boolean isUpdated() {
        return this.updated;
    }

    public boolean isEditing() {
        return this.editing;
    }

    public Part withEditing(boolean editing) {
        return new Part(name, price, updated, editing);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Part part = (Part) o;

        if (price != part.price) return false;
        if (updated != part.updated) return false;
        if (editing != part.editing) return false;
        return name.equals(part.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (int) (price ^ (price >>> 32));
        result = 31 * result + (updated ? 1 : 0);
        result = 31 * result + (editing ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Part{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", updated=" + updated +
                ", editing=" + editing +
                '}';
    }
}
