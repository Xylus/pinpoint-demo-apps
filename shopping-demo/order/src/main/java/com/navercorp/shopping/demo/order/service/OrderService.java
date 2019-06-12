package com.navercorp.shopping.demo.order.service;

import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;

import java.util.List;

/**
 * @author HyunGil Jeong
 */
public interface OrderService {

    OrderInfo getOrder(String orderId);

    String createOrder(List<Integer> productIds);

    boolean processOrder(String orderId, Long paymentAmount);

    void processSuccessfulPayment(String orderId);

    void processFailedPayment(String orderId);

}
