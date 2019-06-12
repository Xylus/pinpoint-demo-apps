package com.navercorp.shopping.demo.batch.service;

import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;

import java.util.List;

/**
 * @author HyunGil Jeong
 */
public interface ApigwService {

    List<ProductInfo> getShoppingProducts();

    OrderInfo createShoppingOrder(List<ProductInfo> products);

    OrderInfo getShoppingOrder(String orderId);

    Boolean processShoppingPayment(String orderId, Long paymentAmount);
}
