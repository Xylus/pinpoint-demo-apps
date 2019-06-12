package com.navercorp.shopping.demo.batch.task;

import com.navercorp.shopping.demo.batch.controller.VendorController;
import com.navercorp.shopping.demo.batch.mapper.OrderMapper;
import com.navercorp.shopping.demo.batch.service.ApigwService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author HyunGil Jeong
 */
@Component
public class ScheduledTasks {

    private static final long SECOND_MS = 1000L;
    private static final long MINUTE_MS = 60 * SECOND_MS;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ApigwService apigwService;
    private final OrderMapper orderMapper;

    @Autowired
    public ScheduledTasks(ApigwService apigwService, OrderMapper orderMapper) {
        this.apigwService = Objects.requireNonNull(apigwService, "apigwService must not be null");
        this.orderMapper = Objects.requireNonNull(orderMapper, "orderMapper must not be null");
    }

    @Scheduled(fixedDelay = 1 * MINUTE_MS)
    public void runShoppingTask() {
        logger.info("shopping task started");
        orderMapper.deleteAllOrders();
        final long shoppingTaskDurationMs = 5 * MINUTE_MS;
        final ShoppingTask shoppingTask = new ShoppingTask(shoppingTaskDurationMs, apigwService);
        final long startTimestamp = System.currentTimeMillis();
        try {
            shoppingTask.run();
        } catch (Exception e) {
            logger.error("Exception while running shopping task", e);
        } finally {
            shoppingTask.shutdown();
            logger.info("shopping task finished in {}ms", (System.currentTimeMillis() - startTimestamp));
            orderMapper.deleteAllOrders();
            VendorController.resetCounter();
        }
    }
}
