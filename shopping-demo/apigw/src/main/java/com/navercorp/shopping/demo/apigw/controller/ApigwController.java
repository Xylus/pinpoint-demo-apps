package com.navercorp.shopping.demo.apigw.controller;

import com.navercorp.shopping.demo.apigw.service.ShoppingService;
import com.navercorp.shopping.demo.commons.vo.order.OrderCreateParam;
import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.order.OrderPaymentParam;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author HyunGil Jeong
 */
@RestController
@RequestMapping("v1")
public class ApigwController {

    private final ShoppingService shoppingService;

    @Autowired
    public ApigwController(ShoppingService shoppingService) {
        this.shoppingService = Objects.requireNonNull(shoppingService, "shoppingService must not be null");
    }

    @GetMapping("/shopping/products")
    public ResponseEntity<List<ProductInfo>> getShoppingProducts() {
        return shoppingService.getProducts();
    }

    @PostMapping("/shopping/orders")
    public ResponseEntity<OrderInfo> createShoppingOrder(@RequestBody OrderCreateParam orderCreateParam) {
        return shoppingService.createOrder(orderCreateParam);
    }

    @GetMapping("/shopping/orders/{orderId}")
    public ResponseEntity<OrderInfo> getShoppingOrder(@PathVariable("orderId") String orderId) {
        return shoppingService.getOrder(orderId);
    }

    @PatchMapping("/shopping/orders/{orderId}")
    public ResponseEntity<Boolean> processShoppingOrder(@PathVariable("orderId") String orderId,
                                                        @RequestBody OrderPaymentParam orderPaymentParam) {
        return shoppingService.processOrder(orderId, orderPaymentParam);
    }
}
