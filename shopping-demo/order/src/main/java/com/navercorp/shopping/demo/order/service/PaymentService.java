package com.navercorp.shopping.demo.order.service;

/**
 * @author HyunGil Jeong
 */
public interface PaymentService {

    Boolean makePayment(String orderId, Long paymentAmount);
}
