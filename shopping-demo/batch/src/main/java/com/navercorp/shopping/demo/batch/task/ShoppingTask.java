package com.navercorp.shopping.demo.batch.task;

import com.navercorp.shopping.demo.batch.service.ApigwService;
import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author HyunGil Jeong
 */
public class ShoppingTask {

    private static final int NUM_THREADS = 5;
    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(NUM_THREADS);

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final long taskDurationMs;
    private final ApigwService apigwService;

    public ShoppingTask(long taskDurationMs, ApigwService apigwService) {
        this.taskDurationMs = taskDurationMs;
        this.apigwService = Objects.requireNonNull(apigwService, "apigwService must not be null");
    }

    public void run() {
        final long minTransactionIntervalMs = 100L;
        final long maxTransactionIntervalMs = 1000L;
        final long startTimestamp = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTimestamp < taskDurationMs) {

            ThreadLocalRandom random = ThreadLocalRandom.current();
            long sleepTimeMs = random.nextLong(maxTransactionIntervalMs - minTransactionIntervalMs) + minTransactionIntervalMs;
            try {
                ShoppingTransactionWorker shoppingTransactionWorker = new ShoppingTransactionWorker();
                executor.execute(shoppingTransactionWorker);
                Thread.sleep(sleepTimeMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }

    private class ShoppingTransactionWorker implements Runnable {
        private final long stepMinDelayMs = 1000L;
        private final long stepMaxDelayMs = 5000L;

        @Override
        public void run() {
            List<ProductInfo> shoppingProducts = getRandomShoppingProducts();
            if (shoppingProducts.isEmpty()) {
                return;
            }
            ThreadLocalRandom random = ThreadLocalRandom.current();
            long delay = random.nextLong(stepMaxDelayMs - stepMinDelayMs) + stepMinDelayMs;
            executor.schedule(new CreateShoppingOrderWorker(shoppingProducts), delay, TimeUnit.MILLISECONDS);
        }

        private class CreateShoppingOrderWorker implements Runnable {

            List<ProductInfo> shoppingProducts;

            private CreateShoppingOrderWorker(List<ProductInfo> shoppingProducts) {
                this.shoppingProducts = Objects.requireNonNull(shoppingProducts, "shoppingProducts must not be null");
            }

            @Override
            public void run() {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                long delay = random.nextLong(stepMaxDelayMs - stepMinDelayMs) + stepMinDelayMs;
                try {
                    OrderInfo shoppingOrder = apigwService.createShoppingOrder(shoppingProducts);
                    if (shoppingOrder == null || shoppingOrder.getOrderId() == null) {
                        return;
                    }
                    executor.schedule(new ProcessShoppingOrderWorker(shoppingOrder.getOrderId()), delay, TimeUnit.MILLISECONDS);
                } catch (RestClientException e) {
                    logger.error("Error creating shopping order", e);
                }
            }
        }

        private class ProcessShoppingOrderWorker implements Runnable {

            private final String orderId;

            private ProcessShoppingOrderWorker(String orderId) {
                this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
            }

            @Override
            public void run() {
                try {
                    OrderInfo shoppingOrder = apigwService.getShoppingOrder(orderId);
                    if (shoppingOrder == null) {
                        return;
                    }
                    Long paymentDue = shoppingOrder.getPaymentAmount();
                    apigwService.processShoppingPayment(orderId, paymentDue);
                } catch (RestClientException e) {
                    logger.error("Error processing payment", e);
                }
            }
        }

        private List<ProductInfo> getRandomShoppingProducts() {
            List<ProductInfo> shoppingProducts = Collections.emptyList();
            try {
                shoppingProducts = apigwService.getShoppingProducts();
            } catch (RestClientException e) {
                logger.error("Error getting shopping products", e);
            }
            if (CollectionUtils.isEmpty(shoppingProducts)) {
                return Collections.emptyList();
            }
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int numItems = random.nextInt(1, 6);
            Collections.shuffle(shoppingProducts);
            if (numItems > shoppingProducts.size()) {
                return shoppingProducts;
            }
            return shoppingProducts.subList(0, numItems);
        }
    }
}
