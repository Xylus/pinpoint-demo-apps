package com.navercorp.shopping.demo.order.controller;

import com.navercorp.shopping.demo.commons.vo.order.OrderCreateParam;
import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.order.OrderPaymentParam;
import com.navercorp.shopping.demo.order.service.OrderService;
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
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = Objects.requireNonNull(orderService, "orderService must not be null");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderInfo> getOrder(@PathVariable("orderId") String orderId) {
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderInfo);
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderCreateParam orderCreateParam) {
        List<Integer> productIds = orderCreateParam.getProductIds();
        if (CollectionUtils.isEmpty(productIds)) {
            return ResponseEntity.badRequest().build();
        }
        String orderId = orderService.createOrder(productIds);
        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/orders/{orderId}")
                .build()
                .expand(orderId).toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Boolean> processOrder(@PathVariable("orderId") String orderId,
                                                @RequestBody OrderPaymentParam orderPaymentParam) {
        Long paymentAmount = orderPaymentParam.getPaymentAmount();
        if (orderService.processOrder(orderId, paymentAmount)) {
            return ResponseEntity.ok(Boolean.TRUE);
        }
        return ResponseEntity.ok(Boolean.FALSE);
    }
}
