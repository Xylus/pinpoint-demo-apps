package com.navercorp.shopping.demo.commons.vo.order;

import java.util.List;

/**
 * @author HyunGil Jeong
 */
public class OrderCreateParam {

    private List<Integer> productIds;

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }
}
