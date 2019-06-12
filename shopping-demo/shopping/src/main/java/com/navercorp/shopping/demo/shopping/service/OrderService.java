package com.navercorp.shopping.demo.shopping.service;

import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;

import java.util.List;

/**
 * @author HyunGil Jeong
 */
public interface OrderService {

    OrderInfo getOrder(String orderId);

    OrderInfo createOrder(List<Integer> productIds);

    Boolean processOrder(String orderId, Long paymentAmount);
}
