package com.github.j5ik2o.ddd_example.purchage_order.domain.ng_case1;

import lombok.NonNull;

public final class PurchaseOrderItem {

    private int id;

    private int quantity;

    @NonNull
    private Part part;

    private boolean updated;

    private boolean editing;

    public PurchaseOrderItem(int id, int quantity, Part part, boolean updated, boolean editing) {
        this.id = id;
        this.quantity = quantity;
        this.part = part;
        this.updated = updated;
        this.editing = editing;
    }

    public PurchaseOrderItem(int id, int quantity, Part part) {
        this(id, quantity, part, false, false);
    }

    public long getAmount() {
        return part.getPrice() * quantity;
    }

    public PurchaseOrderItem withEditing(boolean editing) {
        return new PurchaseOrderItem(id, quantity, part, updated, editing);
    }

    public PurchaseOrderItem withQuantity(int quantity) {
        return new PurchaseOrderItem(id, quantity, part, true, editing);
    }

    public PurchaseOrderItem withPart(Part part) {
        return new PurchaseOrderItem(id, quantity, part, true, editing);
    }

    public int getId() {
        return this.id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    @NonNull
    public Part getPart() {
        return this.part;
    }

    public boolean isUpdated() {
        return this.updated;
    }

    public boolean isEditing() {
        return this.editing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseOrderItem that = (PurchaseOrderItem) o;

        if (id != that.id) return false;
        if (quantity != that.quantity) return false;
        if (updated != that.updated) return false;
        if (editing != that.editing) return false;
        return part.equals(that.part);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + quantity;
        result = 31 * result + part.hashCode();
        result = 31 * result + (updated ? 1 : 0);
        result = 31 * result + (editing ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PurchaseOrderItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", part=" + part +
                ", updated=" + updated +
                ", editing=" + editing +
                '}';
    }
}
