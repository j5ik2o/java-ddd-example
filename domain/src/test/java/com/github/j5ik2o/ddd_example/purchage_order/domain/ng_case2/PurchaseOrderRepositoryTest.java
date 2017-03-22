package com.github.j5ik2o.ddd_example.purchage_order.domain.ng_case2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PurchaseOrderRepositoryTest {

    private void george(PurchaseOrder purchaseOrder) {
        PurchaseOrderItem purchaseOrderItem1 = purchaseOrder
                .getPurchaseOrderItems()
                .get(0);
        Part part1 = purchaseOrderItem1.getPart().withEditing(true);
        purchaseOrder.updatePurchaseOrderItem(purchaseOrderItem1.withPart(part1));

        PurchaseOrderItem purchaseOrderItem2 = purchaseOrder
                .getPurchaseOrderItems()
                .get(1);
        Part part2 = purchaseOrderItem2.getPart().withEditing(true);
        purchaseOrder.updatePurchaseOrderItem(purchaseOrderItem2.withPart(part2));
    }

    private void amanda(PurchaseOrder purchaseOrder) throws InterruptedException {
        PurchaseOrderItem purchaseOrderItem = purchaseOrder
                .getPurchaseOrderItems()
                .get(1);
        while(purchaseOrderItem.isEditing()){
            System.out.println("wating...");
            Thread.sleep(1000);
        }
        Part part = purchaseOrderItem.getPart().withEditing(true);
        purchaseOrder.updatePurchaseOrderItem(purchaseOrderItem.withQuantity(2).withPart(part));
    }

    private void sam(PurchaseOrder purchaseOrder) throws InterruptedException {
        PurchaseOrderItem purchaseOrderItem = purchaseOrder
                .getPurchaseOrderItems()
                .get(1);
        while(purchaseOrderItem.isEditing()){
            System.out.println("wating...");
            Thread.sleep(1000);
        }
        purchaseOrder.updatePurchaseOrderItem(purchaseOrderItem.withQuantity(2).withEditing(true));
    }

    @Test
    public void test() throws InterruptedException {
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
        sam(purchaseOrder);

        System.out.println("purchaseOrder.isOverApprovedLimit = " + purchaseOrder.isOverApprovedLimit());

    }

}