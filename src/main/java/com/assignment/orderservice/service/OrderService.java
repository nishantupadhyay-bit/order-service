package com.assignment.orderservice.service;

import com.assignment.orderservice.exception.InvalidOrderException;
import com.assignment.orderservice.exception.OrderNotFoundException;
import com.assignment.orderservice.model.OrderEntity;
import com.assignment.orderservice.model.OrderStatus;
import com.assignment.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderEntity createOrder(OrderEntity order) {
        validateOrder(order);
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public OrderEntity getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    private void validateOrder(OrderEntity order) {
        if (order == null) {
            throw new InvalidOrderException("Order must not be null");
        }

        if (order.getUserId() == null || order.getUserId() <= 0) {
            throw new InvalidOrderException("User ID must be positive");
        }

        if (order.getProductId() == null || order.getProductId() <= 0) {
            throw new InvalidOrderException("Product ID must be positive");
        }

        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            throw new InvalidOrderException("Quantity must be positive");
        }
    }
}
