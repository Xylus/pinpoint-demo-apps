package com.navercorp.shopping.demo.commons.domain.order;

/**
 * @author HyunGil Jeong
 */
public enum OrderStatus {

    OPEN(0, "Open"),
    COMPLETE(1, "Completed"),
    CANCEL(2, "Cancelled");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus findByCode(int code) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.code == code) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Unknown OrderStatus code : " + code);
    }
}
