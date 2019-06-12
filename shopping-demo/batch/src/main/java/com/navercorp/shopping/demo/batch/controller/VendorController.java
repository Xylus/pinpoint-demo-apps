package com.navercorp.shopping.demo.batch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author HyunGil Jeong
 */
@RestController
public class VendorController {

    private static final Boolean PAYMENT_SUCCESSFUL = Boolean.TRUE;
    private static final Boolean PAYMENT_FAILED = Boolean.FALSE;

    private static final AtomicLong REQUEST_COUNTER = new AtomicLong();
    private static final long REQUEST_THRESHOLD = 100;
    private static final long MIN_SLEEP_TIME_MS = 10L;
    private static final long MAX_SLEEP_TIME_MS = 1000L;
    private static final int THRESHOLD_TRIGGER_CHANCE_PERCENT = 40;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/payment")
    public ResponseEntity<Boolean> processPayment() {
        boolean paymentFailed = false;
        long currentRequestCount = REQUEST_COUNTER.incrementAndGet();
        if (currentRequestCount > REQUEST_THRESHOLD) {
            paymentFailed = ThreadLocalRandom.current().nextInt(100) / THRESHOLD_TRIGGER_CHANCE_PERCENT == 0;
        }
        emulatePayment();
        if (paymentFailed) {
            return ResponseEntity.ok(PAYMENT_FAILED);
        }
        return ResponseEntity.ok(PAYMENT_SUCCESSFUL);
    }

    private void emulatePayment() {
        long sleepTimeMs = ThreadLocalRandom.current().nextLong(MIN_SLEEP_TIME_MS, MAX_SLEEP_TIME_MS);
        sleep(sleepTimeMs);
    }

    private void sleep(long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void resetCounter() {
        REQUEST_COUNTER.set(0);
    }

}
