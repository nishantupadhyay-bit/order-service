package com.assignment.orderservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderEntityTest {

    @Test
    void shouldStoreOrderDetails() {
        OrderEntity order = new OrderEntity();

        order.setUserId(10L);
        order.setProductId(20L);
        order.setQuantity(2);
        order.setStatus(OrderStatus.PENDING);

        assertEquals(10L, order.getUserId());
        assertEquals(20L, order.getProductId());
        assertEquals(2, order.getQuantity());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void shouldInitializeOrderStatus() {
        OrderEntity order = new OrderEntity();

        assertNotNull(order.getStatus());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }
}