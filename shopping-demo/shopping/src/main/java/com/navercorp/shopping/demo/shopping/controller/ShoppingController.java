package com.navercorp.shopping.demo.shopping.controller;

import com.navercorp.shopping.demo.commons.vo.order.OrderCreateParam;
import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.order.OrderPaymentParam;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import com.navercorp.shopping.demo.shopping.service.OrderService;
import com.navercorp.shopping.demo.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * @author HyunGil Jeong
 */
@RestController
@RequestMapping("/shopping")
public class ShoppingController {

    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public ShoppingController(ProductService productService, OrderService orderService) {
        this.productService = Objects.requireNonNull(productService, "productService must not be null");
        this.orderService = Objects.requireNonNull(orderService, "orderService must not be null");
    }

    @GetMapping("/products")
    public List<ProductInfo> getProducts() {
        return productService.getProductInfos();
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderInfo> createOrder(@RequestBody OrderCreateParam orderCreateParam) {
        List<Integer> productIds = orderCreateParam.getProductIds();
        if (CollectionUtils.isEmpty(productIds)) {
            return ResponseEntity.badRequest().build();
        }
        OrderInfo orderInfo = orderService.createOrder(productIds);
        String orderId = orderInfo.getOrderId();
        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/shopping/orders/{orderId}")
                .build()
                .expand(orderId).toUri();
        return ResponseEntity.created(location).body(orderInfo);
    }

    @GetMapping("/orders/{orderId}")
    public OrderInfo getOrder(@PathVariable("orderId") String orderId) {
        return orderService.getOrder(orderId);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<Boolean> processOrder(@PathVariable("orderId") String orderId,
                                                @RequestBody OrderPaymentParam orderPaymentParam) {
        Long paymentAmount = orderPaymentParam.getPaymentAmount();
        return ResponseEntity.ok(orderService.processOrder(orderId, paymentAmount));
    }
}
