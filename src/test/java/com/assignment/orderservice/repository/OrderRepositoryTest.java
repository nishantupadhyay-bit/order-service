package com.assignment.orderservice.repository;

import com.assignment.orderservice.model.OrderEntity;
import com.assignment.orderservice.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveAndRetrieveOrder() {
        OrderEntity order = new OrderEntity();
        order.setUserId(10L);
        order.setProductId(20L);
        order.setQuantity(2);
        order.setStatus(OrderStatus.PENDING);

        OrderEntity savedOrder = orderRepository.save(order);

        assertTrue(savedOrder.getId() > 0);

        OrderEntity retrievedOrder = orderRepository.findById(savedOrder.getId())
                .orElseThrow();

        assertEquals(10L, retrievedOrder.getUserId());
        assertEquals(20L, retrievedOrder.getProductId());
        assertEquals(2, retrievedOrder.getQuantity());
        assertEquals(OrderStatus.PENDING, retrievedOrder.getStatus());
        assertTrue(retrievedOrder.getCreatedAt() != null);
        assertTrue(retrievedOrder.getUpdatedAt() != null);
    }
}