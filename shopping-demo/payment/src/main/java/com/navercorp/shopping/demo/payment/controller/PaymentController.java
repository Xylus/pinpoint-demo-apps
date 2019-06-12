package com.navercorp.shopping.demo.payment.controller;

import com.navercorp.shopping.demo.payment.service.PaymentService;
import com.navercorp.shopping.demo.commons.vo.payment.PaymentForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author HyunGil Jeong
 */
@RestController
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = Objects.requireNonNull(paymentService, "paymentService must not be null");
    }

    @PostMapping("/pay/{orderId}")
    public ResponseEntity<Boolean> pay(@PathVariable("orderId") String orderId,
                                       PaymentForm paymentForm) {
        Long amount = paymentForm.getAmount();
        logger.info("Making payment for {}", orderId);
        boolean paymentSuccess = paymentService.makePayment(amount);
        if (paymentSuccess) {
            return ResponseEntity.ok(Boolean.TRUE);
        }
        return ResponseEntity.ok(Boolean.FALSE);
    }
}
