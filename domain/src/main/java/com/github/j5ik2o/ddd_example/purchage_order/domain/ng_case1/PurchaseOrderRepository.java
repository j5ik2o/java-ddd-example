package com.github.j5ik2o.ddd_example.purchage_order.domain.ng_case1;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PurchaseOrderRepository {

    private final Map<String, PurchaseOrder> entities;

    public PurchaseOrderRepository(Map<String, PurchaseOrder> entities) {
        Validate.notNull(entities);
        this.entities = entities;
    }

    public PurchaseOrderRepository() {
        this(Maps.newHashMap());
    }

    public void store(PurchaseOrder purchaseOrder) {
        Set<Map.Entry<String, PurchaseOrder>> entriesSet = entities.entrySet();
        boolean find = false;
        for(Map.Entry<String, PurchaseOrder> entry : entriesSet) {
            if (entry.getKey().equals(purchaseOrder.getId())) {
                updateItems(purchaseOrder, entry);
                find = true;
                break;
            }
        }
        if (!find) {
            this.entities.put(purchaseOrder.getId(), purchaseOrder);
        }
    }

    private void updateItems(PurchaseOrder purchaseOrder, Map.Entry<String, PurchaseOrder> entry) {
        List<PurchaseOrderItem> purchaseOrderItems = purchaseOrder.getPurchaseOrderItems();
        for(PurchaseOrderItem item : purchaseOrderItems) {
            if (item.isUpdated() && !item.isEditing()) {
                entry.getValue().updatePurchaseOrderItem(item);
            }
        }
    }

    public PurchaseOrder getById(String id) {
        return entities.get(id);
    }

}
