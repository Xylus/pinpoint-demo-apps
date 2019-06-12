package com.navercorp.shopping.demo.order.service;

import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;

/**
 * @author HyunGil Jeong
 */
public interface ProductService {

    ProductInfo getProductInfo(Integer productId);
}
