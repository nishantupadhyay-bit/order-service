package com.assignment.orderservice.grpc;

import com.assignment.orderservice.exception.InvalidOrderException;
import com.assignment.orderservice.exception.OrderNotFoundException;
import com.assignment.orderservice.model.OrderEntity;
import com.assignment.orderservice.model.OrderStatus;
import com.assignment.orderservice.service.OrderService;
import com.assignment.protos.order.v1.CreateOrderRequest;
import com.assignment.protos.order.v1.GetOrderRequest;
import com.assignment.protos.order.v1.ListOrdersRequest;
import com.assignment.protos.order.v1.ListOrdersResponse;
import com.assignment.protos.order.v1.ListUserOrdersRequest;
import com.assignment.protos.order.v1.OrderResponse;
import com.assignment.protos.order.v1.OrderServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import com.google.protobuf.Timestamp;

import java.time.ZoneOffset;
import java.util.List;

@Service
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderService orderService;

    public OrderGrpcService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void createOrder(CreateOrderRequest request,
                            StreamObserver<OrderResponse> observer) {
        try {
            OrderEntity order = new OrderEntity();
            order.setUserId(request.getUserId());
            order.setProductId(request.getProductId());
            order.setQuantity(request.getQuantity());

            observer.onNext(toResponse(orderService.createOrder(order)));
            observer.onCompleted();
        } catch (Exception exception) {
            observer.onError(toStatus(exception).asRuntimeException());
        }
    }

    @Override
    public void getOrder(GetOrderRequest request,
                         StreamObserver<OrderResponse> observer) {
        try {
            observer.onNext(toResponse(orderService.getOrderById(request.getOrderId())));
            observer.onCompleted();
        } catch (Exception exception) {
            observer.onError(toStatus(exception).asRuntimeException());
        }
    }

    @Override
    public void listOrders(ListOrdersRequest request,
                           StreamObserver<ListOrdersResponse> observer) {
        try {
            ListOrdersResponse.Builder response = ListOrdersResponse.newBuilder();

            for (OrderEntity order : orderService.getAllOrders()) {
                response.addOrders(toResponse(order));
            }

            observer.onNext(response.build());
            observer.onCompleted();
        } catch (Exception exception) {
            observer.onError(toStatus(exception).asRuntimeException());
        }
    }

    @Override
    public void listUserOrders(ListUserOrdersRequest request,
                               StreamObserver<ListOrdersResponse> observer) {
        try {
            List<OrderEntity> orders =
                    orderService.getOrdersByUserId(request.getUserId());

            ListOrdersResponse.Builder response = ListOrdersResponse.newBuilder();

            for (OrderEntity order : orders) {
                response.addOrders(toResponse(order));
            }

            observer.onNext(response.build());
            observer.onCompleted();
        } catch (Exception exception) {
            observer.onError(toStatus(exception).asRuntimeException());
        }
    }

    private OrderResponse toResponse(OrderEntity order) {
        return OrderResponse.newBuilder()
                .setId(order.getId())
                .setUserId(order.getUserId())
                .setProductId(order.getProductId())
                .setQuantity(order.getQuantity())
                .setStatus(toProtoStatus(order.getStatus()))
                .setCreatedAt(toTimestamp(order.getCreatedAt()))
                .setUpdatedAt(toTimestamp(order.getUpdatedAt()))
                .build();
    }

    private com.assignment.protos.order.v1.OrderStatus toProtoStatus(
            OrderStatus status) {
        return switch (status) {
            case PENDING ->
                    com.assignment.protos.order.v1.OrderStatus.ORDER_STATUS_PENDING;
            case CONFIRMED ->
                    com.assignment.protos.order.v1.OrderStatus.ORDER_STATUS_CONFIRMED;
            case CANCELLED ->
                    com.assignment.protos.order.v1.OrderStatus.ORDER_STATUS_CANCELLED;
        };
    }

    private Timestamp toTimestamp(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return Timestamp.getDefaultInstance();
        }

        return Timestamp.newBuilder()
                .setSeconds(dateTime.toEpochSecond(ZoneOffset.UTC))
                .setNanos(dateTime.getNano())
                .build();
    }

    private Status toStatus(Exception exception) {
        if (exception instanceof OrderNotFoundException) {
            return Status.NOT_FOUND.withDescription(exception.getMessage());
        }

        if (exception instanceof InvalidOrderException) {
            return Status.INVALID_ARGUMENT.withDescription(exception.getMessage());
        }

        return Status.INTERNAL.withDescription("An unexpected error occurred");
    }
}