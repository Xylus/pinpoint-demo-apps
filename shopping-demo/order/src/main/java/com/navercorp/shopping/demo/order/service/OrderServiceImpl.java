package com.navercorp.shopping.demo.order.service;

import com.navercorp.shopping.demo.commons.domain.order.Order;
import com.navercorp.shopping.demo.commons.domain.order.OrderStatus;
import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import com.navercorp.shopping.demo.order.exception.InvalidOrderException;
import com.navercorp.shopping.demo.order.exception.InvalidPaymentException;
import com.navercorp.shopping.demo.order.exception.OrderNotFoundException;
import com.navercorp.shopping.demo.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author HyunGil Jeong
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(ProductService productService, PaymentService paymentService, OrderMapper orderMapper) {
        this.productService = Objects.requireNonNull(productService, "productService must not be null");
        this.paymentService = Objects.requireNonNull(paymentService, "paymentService must not be null");
        this.orderMapper = Objects.requireNonNull(orderMapper, "orderMapper must not be null");
    }

    @Override
    public OrderInfo getOrder(String orderId) {
        Order order = orderMapper.selectOrder(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return OrderInfo.fromOrder(order);
    }

    @Override
    @Transactional
    public String createOrder(List<Integer> productIds) {
        long paymentAmount = 0;
        for (Integer productId : productIds) {
            ProductInfo productInfo = productService.getProductInfo(productId);
            paymentAmount += productInfo.getProductPrice();
        }
        UUID uuid = UUID.randomUUID();
        String orderId = uuid.toString();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setPaymentAmount(paymentAmount);
        order.setOrderTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.OPEN);
        orderMapper.insertOrder(order);
        return orderId;
    }

    @Override
    @Transactional
    public boolean processOrder(String orderId, Long paymentAmount) {
        Order order = orderMapper.selectOrderForUpdate(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus != OrderStatus.OPEN) {
            throw new InvalidOrderException("Invalid order state for payment, orderId : " + orderId + ", state : " + orderStatus);
        }
        if (!order.getPaymentAmount().equals(paymentAmount)) {
            throw new InvalidPaymentException(order.getPaymentAmount(), paymentAmount);
        }
        boolean paymentSuccessful = paymentService.makePayment(orderId, paymentAmount);
        if (paymentSuccessful) {
            processSuccessfulPayment(orderId);
        } else {
            processFailedPayment(orderId);
        }
        return paymentSuccessful;
    }

    @Override
    public void processSuccessfulPayment(String orderId) {
        orderMapper.updateOrderComplete(orderId);
    }

    @Override
    public void processFailedPayment(String orderId) {
        int delaySeconds = ThreadLocalRandom.current().nextInt(8) + 1;
        orderMapper.updateOrderNotComplete(orderId, delaySeconds);
    }
}
