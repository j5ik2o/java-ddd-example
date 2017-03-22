package com.github.j5ik2o.ddd_example.purchage_order.domain.ng_case1;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PurchaseOrderRepositoryTest {

    private void george(PurchaseOrder purchaseOrder) {
        PurchaseOrderItem purchaseOrderItem = purchaseOrder
                .getPurchaseOrderItems()
                .get(0)
                .withEditing(true)
                .withQuantity(5);
        purchaseOrder.updatePurchaseOrderItem(purchaseOrderItem);
    }

    private void amanda(PurchaseOrder purchaseOrder) {
        PurchaseOrderItem purchaseOrderItem = purchaseOrder
                .getPurchaseOrderItems()
                .get(1)
                .withEditing(true)
                .withQuantity(3);
        purchaseOrder.updatePurchaseOrderItem(purchaseOrderItem);
    }

    @Test
    public void test() {
        PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();

        PurchaseOrder initialPurchaseOrder = new PurchaseOrder("#0012946", 1000);
        Part part1 = new Part("Guitars", 100);
        Part part2 = new Part("Trombones", 200);
        initialPurchaseOrder.addPurchaseOrderItem(new PurchaseOrderItem(1, 3, part1));
        initialPurchaseOrder.addPurchaseOrderItem(new PurchaseOrderItem(2, 2, part2));
        System.out.println("initialPurchaseOrder.isOverApprovedLimit = " + initialPurchaseOrder.isOverApprovedLimit());

        purchaseOrderRepository.store(initialPurchaseOrder);
        PurchaseOrder purchaseOrder = purchaseOrderRepository.getById(initialPurchaseOrder.getId());
        assertEquals(initialPurchaseOrder, purchaseOrder);
        george(purchaseOrder);
        amanda(purchaseOrder);

        System.out.println("purchaseOrder.isOverApprovedLimit = " + purchaseOrder.isOverApprovedLimit());

    }

}