package com.github.j5ik2o.ddd_example.purchage_order.domain.ng_case1;

import com.google.common.collect.Lists;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.util.List;

public final class PurchaseOrder {

    @NonNull
    private final String id;

    private final long approvedLimit;

    @NonNull
    private final List<PurchaseOrderItem> purchaseOrderItems;

    public PurchaseOrder(String id, long approvedLimit, List<PurchaseOrderItem> purchaseOrderItems) {
        Validate.notNull(id);
        Validate.notNull(purchaseOrderItems);
        this.id = id;
        this.approvedLimit = approvedLimit;
        this.purchaseOrderItems = cloneList(purchaseOrderItems);
    }

    public PurchaseOrder(String id, long approvedLimit) {
        this(id, approvedLimit, Lists.newArrayList());
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public long getApprovedLimit() {
        return this.approvedLimit;
    }

    public boolean isOverApprovedLimit() {
        return getTotalPrice() > approvedLimit;
    }

    @NonNull
    public List<PurchaseOrderItem> getPurchaseOrderItems() {
        return cloneList(this.purchaseOrderItems);
    }

    private long getTotalPrice() {
        long totalPrice = 0;
        for (PurchaseOrderItem item : this.purchaseOrderItems) {
            totalPrice += item.getAmount();
        }
        return totalPrice;
    }

    public void addPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        List<PurchaseOrderItem> purchaseOrderItems = (this.purchaseOrderItems);
        purchaseOrderItems.add(purchaseOrderItem);
    }

    public void updatePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        List<PurchaseOrderItem> purchaseOrderItems = (this.purchaseOrderItems);
        for (int index = 0; index < purchaseOrderItems.size(); index++) {
            PurchaseOrderItem item = purchaseOrderItems.get(index);
            if (item.getId() == purchaseOrderItem.getId()) {
                purchaseOrderItems.set(index, purchaseOrderItem);
                break;
            }
        }
    }

    public void removePurchaseOrderItem(int itemId) {
        List<PurchaseOrderItem> purchaseOrderItems = (this.purchaseOrderItems);
        for (int index = 0; index < purchaseOrderItems.size(); index++) {
            PurchaseOrderItem item = purchaseOrderItems.get(index);
            if (item.getId() == itemId) {
                purchaseOrderItems.remove(index);
                break;
            }
        }
    }

    private List<PurchaseOrderItem> cloneList(List<PurchaseOrderItem> items) {
        List<PurchaseOrderItem> result = Lists.newArrayListWithCapacity(items.size());
        for (PurchaseOrderItem item : items) {
            result.add(item);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseOrder that = (PurchaseOrder) o;

        if (approvedLimit != that.approvedLimit) return false;
        if (!id.equals(that.id)) return false;
        return purchaseOrderItems.equals(that.purchaseOrderItems);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (int) (approvedLimit ^ (approvedLimit >>> 32));
        result = 31 * result + purchaseOrderItems.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "id='" + id + '\'' +
                ", approvedLimit=" + approvedLimit +
                ", purchaseOrderItems=" + purchaseOrderItems +
                ", overApprovedLimit=" + isOverApprovedLimit() +
                '}';
    }
}
