package com.navercorp.shopping.demo.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author HyunGil Jeong
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPaymentException extends RuntimeException {

    public InvalidPaymentException(Long expected, Long actual) {
        super("Invalid payment, expected : " + expected + ", actual : " + actual);
    }
}
