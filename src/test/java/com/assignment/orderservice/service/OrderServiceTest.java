package com.assignment.orderservice.service;
import com.assignment.orderservice.exception.InvalidOrderException;
import com.assignment.orderservice.exception.OrderNotFoundException;
import com.assignment.orderservice.model.OrderEntity;
import com.assignment.orderservice.model.OrderStatus;
import com.assignment.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderEntity order;

    @BeforeEach
    void setUp() {
        order = new OrderEntity();
        order.setUserId(10L);
        order.setProductId(20L);
        order.setQuantity(2);
        order.setStatus(OrderStatus.PENDING);
    }

    @Test
    void shouldCreateOrder() {
        when(orderRepository.save(order)).thenReturn(order);

        OrderEntity createdOrder = orderService.createOrder(order);

        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        order.setUserId(null);

        assertThrows(InvalidOrderException.class,
                () -> orderService.createOrder(order));

        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNotPositive() {
        order.setUserId(0L);

        assertThrows(InvalidOrderException.class,
                () -> orderService.createOrder(order));

        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {
        order.setProductId(null);

        assertThrows(InvalidOrderException.class,
                () -> orderService.createOrder(order));

        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNotPositive() {
        order.setProductId(-1L);

        assertThrows(InvalidOrderException.class,
                () -> orderService.createOrder(order));

        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNull() {
        order.setQuantity(null);

        assertThrows(InvalidOrderException.class,
                () -> orderService.createOrder(order));

        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNotPositive() {
        order.setQuantity(0);

        assertThrows(InvalidOrderException.class,
                () -> orderService.createOrder(order));

        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldReturnOrderWhenIdExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderEntity result = orderService.getOrderById(1L);

        assertEquals(order, result);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExist() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderById(99L));
    }

    @Test
    void shouldReturnAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderEntity> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }
}