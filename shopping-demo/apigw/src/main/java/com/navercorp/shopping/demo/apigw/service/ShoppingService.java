package com.navercorp.shopping.demo.apigw.service;

import com.navercorp.shopping.demo.commons.vo.order.OrderCreateParam;
import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.order.OrderPaymentParam;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author HyunGil Jeong
 */
public interface ShoppingService {

    ResponseEntity<List<ProductInfo>> getProducts();

    ResponseEntity<OrderInfo> createOrder(OrderCreateParam orderCreateParam);

    ResponseEntity<OrderInfo> getOrder(String orderId);

    ResponseEntity<Boolean> processOrder(String orderId, OrderPaymentParam orderPaymentParam);
}
