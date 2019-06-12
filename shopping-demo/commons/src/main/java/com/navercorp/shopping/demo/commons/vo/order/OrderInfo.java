package com.navercorp.shopping.demo.commons.vo.order;

import com.navercorp.shopping.demo.commons.domain.order.Order;
import com.navercorp.shopping.demo.commons.domain.order.OrderStatus;

/**
 * @author HyunGil Jeong
 */
public class OrderInfo {

    private String orderId;
    private OrderStatus orderStatus;
    private Long paymentAmount;

    public static OrderInfo fromOrder(Order order) {
        if (order == null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(order.getOrderId());
        orderInfo.setOrderStatus(order.getOrderStatus());
        orderInfo.setPaymentAmount(order.getPaymentAmount());
        return orderInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
