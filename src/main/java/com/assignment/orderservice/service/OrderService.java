package com.assignment.orderservice.service;

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
}